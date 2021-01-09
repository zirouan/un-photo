package com.zirouan.unphoto.data.remote.apihelper

import com.zirouan.unphoto.screen.detail.model.Detail
import com.zirouan.unphoto.screen.photo.model.Photo

interface ApiHelper {

    //region Photos
    suspend fun fetchPhotos(page: Int): List<Photo>
    suspend fun fetchTrackPhoto(id: String): Detail
    //endregion Photos
}