package com.zirouan.unphoto.screen.splash

import androidx.lifecycle.LiveData
import com.zirouan.unphoto.base.BaseContract

interface SplashContract {

    interface ViewModel : BaseContract.ViewModel {
        val screenPhotos: LiveData<Unit>

        fun screenPhotos()
    }

    interface Repository
}