package com.zirouan.unphoto.screen.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.util.exception.ExceptionHandlerHelper

class DetailViewModel(
    private val repository: DetailContract.Repository,
    exception: ExceptionHandlerHelper
) : BaseViewModel(exception), DetailContract.ViewModel {

    override val photo: LiveData<String>
        get() = mPhoto

    private val mPhoto = MutableLiveData<String>()

    override fun fetchTrackPhoto(id: String) {
        defaultLaunch {
            val response = repository.fetchTrackPhoto(id)
            response?.url?.let {
                mPhoto.postValue(it)
            }
        }
    }
}