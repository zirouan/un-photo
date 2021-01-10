package com.zirouan.unphoto.screen.photo

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zirouan.unphoto.data.remote.apihelper.ApiHelper
import com.zirouan.unphoto.screen.photo.model.Photo
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

@ExperimentalCoroutinesApi
class PhotoRepositoryTest{

    companion object {
        private const val PAGE = 1
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val photos: List<Photo> = mockk()
    private val apiHelper: ApiHelper = mockk()
    private val testDispatcher = TestCoroutineDispatcher()
    private var repository: PhotoContract.Repository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = PhotoRepository(apiHelper)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When fetchPhotos in the repository is called, it has to go through the apiHelper fetchPhotos`() {
        coEvery { apiHelper.fetchPhotos(PAGE) } returns photos
        runBlockingTest {
            repository.fetchPhotos(PAGE)
        }

        coVerify { apiHelper.fetchPhotos(PAGE) }
    }
}