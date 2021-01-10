package com.zirouan.unphoto.screen.preview

import android.content.Context
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.navArgs
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.databinding.FragmentPreviewBinding
import com.zirouan.unphoto.util.AnimationUtil
import com.zirouan.unphoto.util.extension.popBackStack
import com.zirouan.unphoto.util.extension.view.loadImage
import com.zirouan.unphoto.util.extension.view.visible
import org.koin.core.module.Module

class PreviewFragment : BaseFragment<FragmentPreviewBinding>() {

    private val mArgs: PreviewFragmentArgs by navArgs()

    override val bindingInflater: (LayoutInflater) -> FragmentPreviewBinding
        get() = FragmentPreviewBinding::inflate

    override val module: Module? = null
    override val viewModel: BaseViewModel? = null

    override fun onInitView() {
        val like = mArgs.argLike
        AnimationUtil.incrementNumberText(mBinding.txtLike, like, 1000)
        AnimationUtil.heartPulse(mBinding.imgHeart, 500, 10)

        val url = mArgs.argUrl
        url?.let { image ->
            mBinding.imgPhoto.loadImage(image, onFinished = {
                mBinding.pbPhoto.visible(it)
            })
        }

        mBinding.imgClose.setOnClickListener {
            popBackStack()
        }

        activity?.let {
            changeStatusBarColor(ContextCompat.getColor(it, android.R.color.transparent))
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.hideStatusBar()
    }

    override fun onDetach() {
        super.onDetach()
        this.showStatusBar()
    }

    override fun onInitObserver() {
    }

    override fun onFetchInitial() {
    }

    override fun onLoading(loading: Boolean) {
    }

    override fun onError(message: String) {
    }
}