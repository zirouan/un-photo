package com.zirouan.unphoto.screen.login

import androidx.lifecycle.LiveData
import com.zirouan.unphoto.base.BaseContract

interface LoginContract {

    interface ViewModel : BaseContract.ViewModel {
        val login: LiveData<Unit>

        fun doLogin(email: String, password: String)
    }

    interface Repository {
        suspend fun doLogin(email: String, password: String)
    }
}