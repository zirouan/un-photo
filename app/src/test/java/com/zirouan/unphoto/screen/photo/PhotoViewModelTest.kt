package com.zirouan.unphoto.screen.photo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.zirouan.unphoto.screen.photo.model.Photo
import com.zirouan.unphoto.util.exception.ExceptionHelper
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@ExperimentalCoroutinesApi
class PhotoViewModelTest {

    companion object {
        private const val PAGE = 1
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    private var photos: List<Photo> = mockk(relaxed = true)

    private var viewModel: PhotoContract.ViewModel  = mockk()
    private var repository: PhotoContract.Repository = mockk()

    private var exception: ExceptionHelper = mockk(relaxed = true)
    private var resetObserver: Observer<Unit> = mockk(relaxed = true)
    private var loadingObserver: Observer<Boolean> = mockk(relaxed = true)
    private var photoObserver: Observer<List<Photo>> = mockk(relaxed = true)

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)

        viewModel = PhotoViewModel(repository, exception)

        viewModel.reset.observeForever(resetObserver)
        viewModel.photos.observeForever(photoObserver)
        viewModel.loading.observeForever(loadingObserver)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When fetchPhotos is called in the ViewModel, check if you called photos in repository`() {
        coEvery { repository.fetchPhotos(PAGE) } returns photos

        viewModel.fetchPhotos(PAGE, isRefresh = true, isInitData = false)
        coVerify { repository.fetchPhotos(PAGE) }
    }

    @Test
    fun `When fetchPhotos is called it returns a list of photos`() {
        coEvery { repository.fetchPhotos(PAGE) } returns photos

        viewModel.fetchPhotos(PAGE, isRefresh = true, isInitData = false)

        runBlockingTest {
            coVerify { resetObserver.onChanged(Unit) }

            repository.fetchPhotos(PAGE)

            coVerify { repository.fetchPhotos(PAGE) }

            coVerify { loadingObserver.onChanged(true) }
            coVerify { photoObserver.onChanged(photos) }
            coVerify { loadingObserver.onChanged(false) }
        }
    }
}
