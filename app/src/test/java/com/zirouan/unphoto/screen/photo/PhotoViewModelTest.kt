package com.zirouan.unphoto.screen.photo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zirouan.unphoto.util.exception.ExceptionHandlerHelper
import com.zirouan.unphoto.screen.photo.model.Photo
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
class PhotoViewModelTest {

    companion object {
        private const val PAGE = 1
    }

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var mViewModel: PhotoContract.ViewModel

    @Mock
    private lateinit var mMessageObserver: Observer<String>

    @Mock
    private lateinit var mLoadingObserver: Observer<Boolean>

    @Mock
    private lateinit var mPhotoObserver: Observer<List<Photo>>

    @Mock
    private lateinit var mResetObserver: Observer<Unit>

    @Mock
    private lateinit var mException: ExceptionHandlerHelper

    @Mock
    private lateinit var mRepository: PhotoContract.Repository

    @Mock
    private var mPhotos: List<Photo> = emptyList()

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        mViewModel = PhotoViewModel(mRepository, mException)

        mViewModel.reset.observeForever(mResetObserver)
        mViewModel.photos.observeForever(mPhotoObserver)
        mViewModel.loading.observeForever(mLoadingObserver)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchPhotos_success() {
        runBlocking {
            mViewModel.fetchPhotos(isRefresh = true)

            launch {
                `when`(mRepository.fetchPhotos(anyInt()))
                        .thenReturn(mPhotos)

                verify(mResetObserver).onChanged(Unit)
                verify(mLoadingObserver).onChanged(true)

                 mRepository.fetchPhotos(PAGE)
                verify(mLoadingObserver).onChanged(false)
            }
        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun fetchPhotos_error() {
        runBlocking {
            mViewModel.fetchPhotos(isRefresh = true)

            launch {
                `when`(mRepository.fetchPhotos(anyInt()))
                        .thenReturn(null)
                
                verify(mResetObserver).onChanged(Unit)
                verify(mLoadingObserver).onChanged(true)

                mRepository.fetchPhotos(PAGE)
                verifyNoMoreInteractions(mPhotoObserver)
                verify(mLoadingObserver).onChanged(false)
            }
        }
    }
}
