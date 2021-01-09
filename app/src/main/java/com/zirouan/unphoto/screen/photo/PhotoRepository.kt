package com.zirouan.unphoto.screen.photo

import com.zirouan.unphoto.data.remote.apihelper.ApiHelper

class PhotoRepository(
    private val apiHelper: ApiHelper
) : PhotoContract.Repository {

    override suspend fun fetchPhotos(page: Int) = apiHelper.fetchPhotos(page)
}