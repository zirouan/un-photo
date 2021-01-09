package com.zirouan.unphoto.data.remote.apihelper

import com.zirouan.unphoto.BuildConfig
import com.zirouan.unphoto.data.remote.api.Api
import com.zirouan.unphoto.screen.photo.model.Photo

class ApiHelperImpl(
    private val api: Api
) : ApiHelper {

    override suspend fun fetchPhotos(page: Int): List<Photo> {
        return api.fetchPhotos(page, BuildConfig.CLIENT_ID)
    }


    override suspend fun fetchTrackPhoto(id: String) =
        api.fetchTrackPhoto(id, BuildConfig.CLIENT_ID)
    //endregion Login
}