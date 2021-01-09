package com.zirouan.unphoto.util.exception

import androidx.annotation.StringRes
import com.zirouan.unphoto.util.exception.model.ErrorMessage

interface ExceptionHandlerHelper {

    fun getErrorMessage(
        exception: Throwable,
        readApiMessage: Boolean? = true,
        @StringRes defaultMessageRes: Int? = null
    ): ErrorMessage

}