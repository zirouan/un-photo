package com.zirouan.unphoto.screen.photo

import android.view.LayoutInflater
import com.zirouan.unphoto.base.BaseViewAdapter
import com.zirouan.unphoto.databinding.FragmentPhotoItemBinding
import com.zirouan.unphoto.screen.photo.model.Photo
import com.zirouan.unphoto.util.AnimationUtil
import com.zirouan.unphoto.util.extension.view.loadImage
import com.zirouan.unphoto.util.extension.view.visible

class PhotoAdapter : BaseViewAdapter<Photo, FragmentPhotoItemBinding>() {

    override val bindingInflater: (LayoutInflater) -> FragmentPhotoItemBinding
        get() = FragmentPhotoItemBinding::inflate

    override fun onBindViewHolderBase(holder: ViewHolder?, position: Int) {
        dataView(holder, data[position])
    }

    private fun dataView(holder: ViewHolder?, photo: Photo) {
        holder.apply {
            photo.urls?.small?.let { small ->
                holder?.binding?.imgPhoto.loadImage(small, onFinished = {
                    holder?.binding?.pbPhoto?.visible(it)
                })
            }

            photo.likes.let {
                if (!photo.animation) {
                    photo.animation = true

                    holder?.binding?.imgHeart?.let { img ->
                        AnimationUtil.heartPulse(img)
                    }

                    holder?.binding?.txtLike?.let { like ->
                        AnimationUtil.incrementNumberText(like, it)
                    }
                }
            }
        }
    }
}
