package com.zirouan.unphoto.screen.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zirouan.unphoto.util.exception.ExceptionHandlerHelper
import com.zirouan.unphoto.screen.detail.model.Detail
import com.zirouan.unphoto.screen.photo.PhotoContract
import com.zirouan.unphoto.screen.photo.PhotoViewModel
import com.zirouan.unphoto.screen.photo.PhotoViewModelTest
import com.zirouan.unphoto.screen.photo.model.Photo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
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
@ExperimentalCoroutinesApi
class DetailViewModelTest {

    companion object {
        private const val PHOTO_ID = "gOA4bYh_flw"
        private const val URL =
            "https://images.unsplash.com/photo-1609857969348-0af51982dd1a?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb"

    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var viewModel: DetailContract.ViewModel = mockk()
    private var repository: DetailContract.Repository = mockk()

    @MockK(relaxed = true)
    private var detail = Detail(URL)

    private var photoObserver: Observer<String> = mockk(relaxed = true)
    private var exception: ExceptionHandlerHelper = mockk(relaxed = true)
    private var loadingObserver: Observer<Boolean> = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = DetailViewModel(repository, exception)

        viewModel.photo.observeForever(photoObserver)
        viewModel.loading.observeForever(loadingObserver)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When fetchTrackPhoto is called in the ViewModel, check if you called photos in repository`() {
        coEvery { repository.fetchTrackPhoto(PHOTO_ID) } returns detail

        viewModel.fetchTrackPhoto(PHOTO_ID)
        coVerify { repository.fetchTrackPhoto(PHOTO_ID) }
    }

    @Test
    fun `When fetchTrackPhoto is called it returns a url`() {
        coEvery { repository.fetchTrackPhoto(PHOTO_ID) } returns detail

        viewModel.fetchTrackPhoto(PHOTO_ID)

        runBlockingTest {
            coVerify { loadingObserver.onChanged(true) }

            repository.fetchTrackPhoto(PHOTO_ID)

            coVerify { repository.fetchTrackPhoto(PHOTO_ID) }

            coVerify { photoObserver.onChanged(detail.url) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }
}
