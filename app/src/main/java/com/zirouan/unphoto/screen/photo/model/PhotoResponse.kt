package com.zirouan.unphoto.screen.photo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoResponse(
    var photos: List<Photo>? = emptyList()
) : Parcelable