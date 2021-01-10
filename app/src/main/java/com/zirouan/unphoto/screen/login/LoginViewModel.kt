package com.zirouan.unphoto.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.util.exception.ExceptionHelper
import kotlinx.coroutines.delay

class LoginViewModel(
        private val repository: LoginContract.Repository,
        exception: ExceptionHelper,
) : BaseViewModel(exception), LoginContract.ViewModel {

    override val screenPhotos: LiveData<Unit>
        get() = mScreenPhotos

    private val mScreenPhotos = MutableLiveData<Unit>()

    override fun screenPhotos() {
        defaultLaunch {
            delay(2000)
            mScreenPhotos.postValue(Unit)
        }
    }
}