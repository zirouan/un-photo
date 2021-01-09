package com.zirouan.unphoto.screen.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zirouan.unphoto.util.exception.ExceptionHandlerHelper
import com.zirouan.unphoto.screen.detail.model.Detail
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {

    companion object {
        private const val PHOTO_ID = "gOA4bYh_flw"
        private const val URL =
            "https://images.unsplash.com/photo-1609857969348-0af51982dd1a?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb"

    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mViewModel: DetailContract.ViewModel

    @Mock
    private lateinit var mLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var mPhotoObserver: Observer<String>

    @Mock
    private lateinit var mException: ExceptionHandlerHelper

    @Mock
    private lateinit var mRepository: DetailContract.Repository

    @Mock
    private lateinit var mDetail: Detail

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mViewModel = DetailViewModel(mRepository, mException)

        mViewModel.photo.observeForever(mPhotoObserver)
        mViewModel.loading.observeForever(mLoadingObserver)

    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchTrackPhoto_success() {
        runBlocking {
            mViewModel.fetchTrackPhoto(PHOTO_ID)

            launch {
                `when`(mRepository.fetchTrackPhoto(anyString()))
                    .thenReturn(mDetail)

                verify(mLoadingObserver).onChanged(true)

                val response = mRepository.fetchTrackPhoto(PHOTO_ID)
                response?.url?.let {
                    verify(mPhotoObserver).onChanged(URL)
                }

                verify(mLoadingObserver).onChanged(false)
            }
        }
    }
}
