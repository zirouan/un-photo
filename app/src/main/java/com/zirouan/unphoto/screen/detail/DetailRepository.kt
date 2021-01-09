package com.zirouan.unphoto.screen.detail

import com.zirouan.unphoto.data.remote.apihelper.ApiHelper

class DetailRepository(
    private val apiHelper: ApiHelper
) : DetailContract.Repository {

    override suspend fun fetchTrackPhoto(id: String) =
        apiHelper.fetchTrackPhoto(id)
}