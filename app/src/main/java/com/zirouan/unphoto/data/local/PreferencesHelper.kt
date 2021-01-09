package com.zirouan.unphoto.data.local

interface PreferencesHelper {

    companion object {
        const val TOKEN = "token"
    }

    fun token(): String?
    fun saveToken(token: String?)
}