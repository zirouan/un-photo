package com.zirouan.unphoto.screen.photo.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Url(
    val small: String = "",
    val full: String = ""
) : Parcelable