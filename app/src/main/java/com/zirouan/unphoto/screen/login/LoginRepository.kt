package com.zirouan.unphoto.screen.login

import com.zirouan.unphoto.data.local.PreferencesHelper

class LoginRepository(
    private val preferencesHelper: PreferencesHelper
) : LoginContract.Repository {

    override suspend fun doLogin(email: String, password: String) {
    }
}