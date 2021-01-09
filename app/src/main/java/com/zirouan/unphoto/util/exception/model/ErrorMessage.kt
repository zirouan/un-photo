package com.zirouan.unphoto.util.exception.model

data class ErrorMessage(
    val message: String,
    val code: Int = -1
)