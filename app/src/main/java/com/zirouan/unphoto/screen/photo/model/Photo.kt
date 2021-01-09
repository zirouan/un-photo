package com.zirouan.unphoto.screen.photo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
    val id: String,
    val color: String,

    val urls: Url?,
    var user: User?,

    val likes: Int = 0,

    @SerializedName("alt_description")
    val altDescription: String? = "",

    var animation: Boolean = false
) : Parcelable