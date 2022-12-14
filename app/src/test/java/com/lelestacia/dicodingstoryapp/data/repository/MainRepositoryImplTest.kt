package com.lelestacia.dicodingstoryapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.database.StoryDatabase
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.data.model.mapper.DataMapper
import com.lelestacia.dicodingstoryapp.ui.main_activity.StoryPagingAdapter
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Requirement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MainRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var database: StoryDatabase
    private lateinit var apiService: DicodingAPI
    private lateinit var repository: MainRepositoryImpl
    private lateinit var photo: File

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Before
    fun setup() {
        context = Module.getContext()
        database = Module.getDatabase()
        apiService = Module.getRetrofit()
        repository = MainRepositoryImpl(apiService, context, database)
        photo = Requirement.getPhoto()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Login failed due to wrong email or password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com", password = "kamil"
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Login failed due to empty email or password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword("", "")
        Assert.assertTrue(
            actualResult is NetworkResponse.GenericException
        )
    }

    @Test
    fun `Successfully logged in with the correct email and password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com", password = "kamilmalik"
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Failed Registration`() = runTest {
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil", email = "km8003296@gmail.com", password = "kamilmalik"
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successful Registration`() = runTest {
        val randomString = java.util.UUID.randomUUID().toString()
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "$randomString@ymail.com",
            password = "kamilmalik"
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Failed to get story list due to wrong token`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(token = "token_salah")
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Managed to get the whole story with location`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(Requirement.getToken())
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Failed to upload due to empty description`() = runTest {
        val actualResult = repository.uploadStory(
            photo = photo,
            description = "",
            token = Requirement.getToken()
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successfully upload image without location`() = runTest {
        val actualResult = repository.uploadStory(
            photo = photo,
            description = "Unit Test",
            token = Requirement.getToken()
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Successful upload Image with location`() = runTest {
        val position = LatLng(-1.6128372919924492, 103.53365088692506)
        val actualResult = repository.uploadStory(
            photo = photo,
            description = "Unit Test with Location",
            lat = position.latitude.toFloat(),
            long = position.longitude.toFloat(),
            token = Requirement.getToken()
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Successfully got story by page`() = runTest {
        val fakeRepository = Mockito.mock(MainRepository::class.java)
        val actualStory = apiService.getStoriesWithPage(
            token = Requirement.getToken(),
            page = 1,
            size = 10
        )
        val listStories = arrayListOf<LocalStory>()
        actualStory.listNetworkStory.forEach {
            listStories.add(DataMapper.mapStory(it))
        }
        val dummy = StoryPagingSourceTest.snapshot(listStories)
        val testingLiveData = MutableLiveData<PagingData<LocalStory>>()
        testingLiveData.value = dummy
        `when`(fakeRepository.getStoriesWithPagination("test_token")).thenReturn(testingLiveData as LiveData<PagingData<LocalStory>>)
        val actualResult = fakeRepository.getStoriesWithPagination("test_token")
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = Module.noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main
        )
        differ.submitData(actualResult.getOrAwaitValue())
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(listStories.size, differ.snapshot().size)
        Assert.assertEquals(listStories, differ.snapshot())
        Assert.assertEquals(listStories[0], differ.snapshot()[0])
    }
}

