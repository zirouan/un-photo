package com.zirouan.unphoto.screen.login

import androidx.lifecycle.LiveData
import com.zirouan.unphoto.base.BaseContract

interface LoginContract {

    interface ViewModel : BaseContract.ViewModel {
        val screenPhotos: LiveData<Unit>

        fun screenPhotos()
    }

    interface Repository
}