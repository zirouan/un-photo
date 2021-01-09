package com.zirouan.unphoto.screen.photo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zirouan.unphoto.base.BaseAdapter
import com.zirouan.unphoto.databinding.FragmentPhotoItemBinding
import com.zirouan.unphoto.screen.photo.model.Photo
import com.zirouan.unphoto.util.AnimationUtil
import com.zirouan.unphoto.util.extension.view.loadImage
import com.zirouan.unphoto.util.extension.view.visible

class PhotoAdapter : BaseAdapter<Photo>() {

    inner class RepoViewHolder(val binding: FragmentPhotoItemBinding) :
            RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolderBase(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        viewType: Int
    ): RecyclerView.ViewHolder {
        return RepoViewHolder(
            FragmentPhotoItemBinding.inflate(
                LayoutInflater.from(parent?.context), parent, false
            )
        )
    }

    override fun onBindViewHolderBase(holder: RecyclerView.ViewHolder?, position: Int) {
        if (holder is RepoViewHolder) {
            setDataView(holder, data[position])
        }
    }

    private fun setDataView(holder: RepoViewHolder, photo: Photo) {
        holder.apply {
            holder.binding.let { binding ->
                photo.urls?.small?.let { small ->
                    binding.imgPhoto.loadImage(small, onFinished = {
                        binding.pbPhoto.visible(it)
                    })
                }

                photo.likes.let {
                    if (!photo.animation) {
                        photo.animation = true
                        AnimationUtil.heartPulse(binding.imgHeart)
                        AnimationUtil.incrementNumberText(binding.txtLike, it)
                    }
                }
            }
        }
    }
}