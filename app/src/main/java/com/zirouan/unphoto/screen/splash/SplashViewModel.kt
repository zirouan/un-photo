package com.zirouan.unphoto.screen.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.util.exception.ExceptionHandlerHelper
import kotlinx.coroutines.delay

class SplashViewModel(
        private val repository: SplashContract.Repository,
        private val exception: ExceptionHandlerHelper,
) : BaseViewModel(exception), SplashContract.ViewModel {

    override val screenPhotos: LiveData<Unit>
        get() = mScreenPhotos

    private val mScreenPhotos = MutableLiveData<Unit>()

    override fun screenPhotos() {
        defaultLaunch {
            delay(1000)
            mScreenPhotos.postValue(Unit)
        }
    }
}