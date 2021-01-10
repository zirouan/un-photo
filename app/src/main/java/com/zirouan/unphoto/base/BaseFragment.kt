package com.zirouan.unphoto.base

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import androidx.annotation.DrawableRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.zirouan.unphoto.R
import com.zirouan.unphoto.util.OnActivityResultCallback
import com.zirouan.unphoto.util.OnEventReceivedListener
import com.zirouan.unphoto.util.extension.*
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import java.net.HttpURLConnection

@Suppress("UNCHECKED_CAST")
abstract class BaseFragment<VB : ViewBinding> : Fragment(), FragmentCompat {

    private var mIsLayoutCreated = false
    private var mLayoutView: View? = null

    private var mViewBinding: ViewBinding? = null
    private var mBaseViewModel: BaseViewModel? = null

    abstract val bindingInflater: (LayoutInflater) -> VB

    protected val mBinding: VB
        get() = mViewBinding as VB

    private var mOnEventReceivedListener: OnEventReceivedListener? = { code, data ->
        onEventReceived(code, data)
    }
    var onActivityResultCallback: OnActivityResultCallback? = null
    open fun onEventReceived(code: Int, data: Any?) {}

    //region Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        loadModule()
        super.onCreate(savedInstanceState)

        mBaseViewModel = viewModel

        setHasOptionsMenu(true)
        initDefaultObservers()
        onInitObserver()
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
        if (!mIsLayoutCreated) {
            onInitView()
            onFetchInitial()
            mIsLayoutCreated = true
        }
    }

    //endregion Fragment

    //region Local
    private fun initDefaultObservers() {
        mBaseViewModel?.redirect?.observe(this, { code ->
            activity?.let {
                when (code) {
                    HttpURLConnection.HTTP_UNAUTHORIZED -> {
                    }
                }
            }
        })
        mBaseViewModel?.loading?.observe(this, { isLoading ->
            onLoading(isLoading)
        })
        mBaseViewModel?.message?.observe(this, { message ->
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

    //endregion Local

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            popBackStack()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    protected fun showNavigationBottom() {
        baseActivity?.changeNavigationVisibilityListener?.invoke(true)
    }

    protected fun hideNavigationBottom() {
        baseActivity?.changeNavigationVisibilityListener?.invoke(false)
    }

    protected fun sendEvent(code: Int, data: Any? = null) {
        baseActivity?.callOnEventReceived(code, data)
    }

    //region toolbar/appBar
    private var toolBarIcon: Int = -1
    private var toolBar: Toolbar? = null
    private var displayHome: Boolean = true
    private var toolBarTitle: Int = R.string.clear
    private var toolBarTitleText: String = ""
    private var toolBarTitleColor: Int = R.color.colorSecondary

    fun setToolbar(toolBar: Toolbar?): BaseFragment<VB> {
        this.toolBar = toolBar
        return this
    }

    fun title(title: Int): BaseFragment<VB> {
        this.toolBarTitle = title
        return this
    }

    fun title(title: String): BaseFragment<VB> {
        this.toolBarTitleText = title
        return this
    }

    fun icon(@DrawableRes icon: Int): BaseFragment<VB> {
        this.toolBarIcon = icon
        return this
    }

    fun displayHome(displayHome: Boolean): BaseFragment<VB> {
        this.displayHome = displayHome
        return this
    }

    fun changeNavigationIcon(@DrawableRes icon: Int) {
        activity?.let {
            this.toolBar?.navigationIcon = ContextCompat.getDrawable(it, icon)
        }
    }

    fun builder() {
        activity?.let {
            (activity as AppCompatActivity?)?.let { appCompat ->
                toolBar?.let { toolbar ->
                    appCompat.setSupportActionBar(toolbar)
                    appCompat.supportActionBar?.let { actionBar ->

                        if (toolBarTitle != -1) {
                            actionBar.title = if (toolBarTitleText != "")
                                toolBarTitleText
                            else
                                getString(toolBarTitle)
                        }

                        actionBar.setDisplayShowHomeEnabled(displayHome)
                        actionBar.setDisplayHomeAsUpEnabled(displayHome)
                        actionBar.elevation = 0f

                        toolbar.setTitleTextColor(ContextCompat.getColor(it, toolBarTitleColor))

                        if (toolBarIcon != -1 && displayHome) {
                            toolbar.navigationIcon = ContextCompat.getDrawable(it, toolBarIcon)
                        }
                    }
                }
            }
        }
    }
    //endregion toolbar/appBar

    //region StatusBar
    protected fun changeStatusBarColor(color: Int) {
        activity?.let {
            if (isAtLeastMarshmallow()) {
                it.window.statusBarColor = color
                changeStatusBarIconsColor(color != Color.WHITE)
            } else if (isAtLeastLollipop()) {
                it.window.statusBarColor = color
            }
        }
    }

    private fun changeStatusBarIconsColor(toWhite: Boolean) {
        activity?.let {
            if (isAtLeastR()) {
                if (toWhite) {
                    it.window.insetsController?.setSystemBarsAppearance(
                        0,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                } else {
                    it.window.insetsController?.setSystemBarsAppearance(
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                        WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                    )
                }
            } else {
                @Suppress("DEPRECATION")
                it.window.decorView.systemUiVisibility = if (
                    toWhite || Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                ) {
                    0
                } else {
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }

    fun hideStatusBar() {
        @Suppress("DEPRECATION")
        if (isAtLeastR()) {
            activity?.window?.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            activity?.window?.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun showStatusBar() {
        @Suppress("DEPRECATION")
        if (isAtLeastR()) {
            activity?.window?.insetsController?.show(WindowInsets.Type.statusBars())
        } else {
            activity?.window?.clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }
    //endregion StatusBar

    override fun onDestroy() {
        super.onDestroy()
        mViewBinding = null
        baseActivity?.removeOnEventReceivedListener(mOnEventReceivedListener)
        mOnEventReceivedListener = null
        onActivityResultCallback = null
    }
}