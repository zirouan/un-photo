package com.zirouan.unphoto.util.exception

import com.zirouan.unphoto.util.exception.model.ErrorMessage

interface ExceptionHandlerHelper {

    fun getErrorMessage(
        exception: Throwable,
        readApiMessage: Boolean? = true,
        defaultMessageRes: Int? = null
    ): ErrorMessage

}