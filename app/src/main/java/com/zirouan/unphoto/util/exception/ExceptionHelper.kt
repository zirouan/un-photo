package com.zirouan.unphoto.util.exception

import com.zirouan.unphoto.util.exception.model.ErrorMessage

interface ExceptionHelper {

    fun message(
        exception: Throwable,
        readApiMessage: Boolean? = true,
        defaultMessageRes: Int? = null
    ): ErrorMessage

}