package com.zirouan.unphoto.screen.detail.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Detail(
    var url: String?
) : Parcelable