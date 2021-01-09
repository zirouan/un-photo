package com.zirouan.unphoto.screen.splash

import android.content.Context
import android.view.LayoutInflater
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseFragment
import com.zirouan.unphoto.databinding.FragmentSplashBinding
import com.zirouan.unphoto.util.extension.TransitionAnimation
import com.zirouan.unphoto.util.extension.navigate
import com.zirouan.unphoto.util.extension.view.loadImage
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override val module = splashModule
    override val viewModel: SplashViewModel by viewModel()
    override val bindingInflater: (LayoutInflater) -> FragmentSplashBinding =
        FragmentSplashBinding::inflate


    //region BaseFragment
    override fun initObservers() {
        viewModel.screenPhotos.observe(this, {
            showScreenPhoto()
        })
    }

    override fun initView() {
        mBinding.imgLogo.loadImage(R.drawable.ic_un_splash)
    }

    override fun fetchInitialData() {
        viewModel.screenPhotos()
    }

    override fun onLoading(isLoading: Boolean) {}

    override fun onError(message: String) {}
    //endregion BaseFragment

    //region Local
    private fun showScreenPhoto() {
        val direction = SplashFragmentDirections.actionSplashHomeFragment()
        navigate(
            directions = direction,
            clearBackStack = true,
            animation = TransitionAnimation.TRANSLATE_FROM_DOWN
        )
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.hideStatusBar()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.showStatusBar()
    }
    //region Local
}