package com.zirouan.unphoto.screen.photo.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("first_name")
    val firstName: String? = "",

    @SerializedName("last_name")
    val lastName: String? = "",

    val bio: String? = ""
) : Parcelable