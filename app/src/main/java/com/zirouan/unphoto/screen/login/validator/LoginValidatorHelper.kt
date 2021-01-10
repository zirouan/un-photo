package com.zirouan.unphoto.screen.login.validator

import android.content.Context
import com.zirouan.unphoto.R
import com.zirouan.unphoto.base.BaseValidatorHelper

class LoginValidatorHelper(private val context: Context) : BaseValidatorHelper {

    override fun validate(vararg any: Any?): String? {
        val email = any[0] as? String
        val password = any[1] as? String

        return when {
            email.isNullOrEmpty() ->
                context.getString(R.string.type_your_email)
            password.isNullOrEmpty() ->
                context.getString(R.string.type_your_password)
            else -> null
        }
    }
}