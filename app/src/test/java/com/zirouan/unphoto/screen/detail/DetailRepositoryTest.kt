package com.zirouan.unphoto.screen.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.zirouan.unphoto.data.remote.apihelper.ApiHelper
import com.zirouan.unphoto.screen.detail.model.Detail
import com.zirouan.unphoto.screen.photo.model.Photo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
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
class DetailRepositoryTest {

    companion object {
        private const val PHOTO_ID = "gOA4bYh_flw"
        private const val URL =
            "https://images.unsplash.com/photo-1609857969348-0af51982dd1a?ixlib=rb-1.2.1&q=85&fm=jpg&crop=entropy&cs=srgb"
    }

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @MockK(relaxed = true)
    private var detail = Detail(URL)

    private val apiHelper: ApiHelper = mockk()
    private val testDispatcher = TestCoroutineDispatcher()
    private var repository: DetailContract.Repository = mockk()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        repository = DetailRepository(apiHelper)
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun `When fetchTrackPhoto in the repository is called, it has to go through the apiHelper fetchTrackPhoto`() {
        coEvery { apiHelper.fetchTrackPhoto(PHOTO_ID) } returns detail
        runBlockingTest {
            repository.fetchTrackPhoto(PHOTO_ID)
        }

        coVerify { apiHelper.fetchTrackPhoto(PHOTO_ID) }
    }
}