package com.zirouan.unphoto.screen.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zirouan.unphoto.base.BaseValidatorHelper
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.util.exception.ExceptionHelper
import kotlinx.coroutines.delay

class LoginViewModel(
        private val repository: LoginContract.Repository,
        private val validator: BaseValidatorHelper,
        exception: ExceptionHelper,
) : BaseViewModel(exception), LoginContract.ViewModel {

    override val login: LiveData<Unit>
        get() = mLogin

    private val mLogin = MutableLiveData<Unit>()

    override fun doLogin(email: String, password: String) {
        defaultLaunch(validator, email, password) {
            delay(2000)
            mLogin.postValue(Unit)
        }
    }
}