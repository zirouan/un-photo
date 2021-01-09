package com.zirouan.unphoto.base

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.zirouan.unphoto.R
import com.zirouan.unphoto.util.OnActivityResultCallback
import com.zirouan.unphoto.util.extension.TransitionAnimation
import com.zirouan.unphoto.util.extension.hideKeyboard
import com.zirouan.unphoto.util.extension.navigate
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules

@Suppress("UNCHECKED_CAST")
abstract class BaseDialogFragment<VB: ViewBinding> : DialogFragment(), FragmentCompat {

    private var mIsLayoutCreated = false
    private var mLayoutView: View? = null

    private var mViewBinding: ViewBinding? = null

    abstract val bindingInflater: (LayoutInflater) -> VB

    protected val mBinding: VB
        get() = mViewBinding as VB

    var onActivityResultCallback: OnActivityResultCallback? = null

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        loadModule()
        initDefaultObservers()

        super.onCreate(savedInstanceState)
        isCancelable = false
        initObservers()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (mLayoutView == null) {
            mViewBinding = bindingInflater.invoke(inflater)
            mLayoutView = requireNotNull(mViewBinding).root
        } else {
            (mLayoutView?.parent as? ViewGroup)?.removeView(mLayoutView)
        }

        hideKeyboard()
        return mLayoutView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.window?.setBackgroundDrawableResource(R.drawable.shape_rectangle_rounded_gray)

        if (!mIsLayoutCreated) {
            initView()
            fetchInitialData()
            mIsLayoutCreated = true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        onActivityResultCallback?.invoke(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        onActivityResultCallback = null
    }
    //endregion Fragment

    //region Local
    private fun initDefaultObservers() {
        viewModel?.redirect?.observe(this, { destination ->
            val rootFragment = if (destination == R.id.splashFragment) getRootParent() else this
            navigate(destination, TransitionAnimation.FADE, null, true, rootFragment)
        })
        viewModel?.loading?.observe(this, { isLoading ->
            onLoading(isLoading)
        })
        viewModel?.message?.observe(this, { message ->
            onError(message)
        })
    }

    private fun loadModule() {
        try {
            module?.let {
                unloadKoinModules(it)
                loadKoinModules(it)
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
        }
    }

    private fun getRootParent(): Fragment {
        var rootParent = parentFragment

        while (rootParent?.parentFragment != null) {
            rootParent = rootParent.parentFragment
        }

        return rootParent ?: this
    }
    //endregion Local

}