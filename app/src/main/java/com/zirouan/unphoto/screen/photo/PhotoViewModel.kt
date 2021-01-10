package com.zirouan.unphoto.screen.photo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.zirouan.unphoto.base.BaseViewModel
import com.zirouan.unphoto.screen.photo.model.Photo
import com.zirouan.unphoto.util.exception.ExceptionHelper

class PhotoViewModel(
        private val repository: PhotoContract.Repository,
        exception: ExceptionHelper
) : BaseViewModel(exception), PhotoContract.ViewModel {

    override val page: LiveData<Int>
        get() = mPage

    override val reset: LiveData<Unit>
        get() = mReset

    override val photos: LiveData<List<Photo>>
        get() = mPhotos

    private val mPage = MutableLiveData<Int>()
    private val mReset = MutableLiveData<Unit>()
    private val mPhotos = MutableLiveData<List<Photo>>()

    private var mCurrentPage: Int = 1
    private val mPhotoList: MutableList<Photo> = mutableListOf()

    override fun fetchPhotos(page: Int, isRefresh: Boolean, isInitData: Boolean) {

        defaultLaunch{
            if (isRefresh) {
                mReset.postValue(Unit)

                /* TODO O Ciclo de vida do ViewModel é maior que o clico de vida da view,
                        caso haja rotação, não será feito uma nova request
                 */
                if (isInitData && mPhotoList.isNotEmpty()) {
                    mPhotos.postValue(mPhotoList)
                    mPage.postValue(mCurrentPage)
                    return@defaultLaunch
                } else {
                    mPhotoList.clear()
                }
            }

            val response = repository.fetchPhotos(page)
            response?.let {
                mCurrentPage = page
                it.let {photos ->
                    mPhotoList.addAll(photos)
                }
                mPhotos.postValue(it)
            }
        }
    }
}