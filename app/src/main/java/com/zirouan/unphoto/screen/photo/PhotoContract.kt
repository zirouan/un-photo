package com.zirouan.unphoto.screen.photo

import androidx.lifecycle.LiveData
import com.zirouan.unphoto.base.BaseContract
import com.zirouan.unphoto.screen.photo.model.Photo

interface PhotoContract {

    interface ViewModel : BaseContract.ViewModel {
        val page: LiveData<Int>
        val reset: LiveData<Unit>
        val photos: LiveData<List<Photo>>

        fun fetchPhotos(page: Int = 1,
            isRefresh: Boolean = false, isInitData: Boolean = true
        )
    }

    interface Repository {
        suspend fun fetchPhotos(page: Int): List<Photo>?
    }
}