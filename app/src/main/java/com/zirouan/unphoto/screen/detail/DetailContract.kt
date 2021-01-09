package com.zirouan.unphoto.screen.detail

import androidx.lifecycle.LiveData
import com.zirouan.unphoto.base.BaseContract
import com.zirouan.unphoto.screen.detail.model.Detail

interface DetailContract {

    interface ViewModel : BaseContract.ViewModel {
        val photo: LiveData<String>

        fun fetchTrackPhoto(id: String)
    }

    interface Repository {
        suspend fun fetchTrackPhoto(id: String): Detail?
    }
}