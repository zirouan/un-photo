package com.zirouan.unphoto.base

import androidx.lifecycle.LiveData

interface BaseContract {

    interface ViewModel {
        val message: LiveData<String>
        val loading: LiveData<Boolean>
    }
}