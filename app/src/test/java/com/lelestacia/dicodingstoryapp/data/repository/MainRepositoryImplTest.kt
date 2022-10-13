package com.lelestacia.dicodingstoryapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.database.StoryDatabase
import com.lelestacia.dicodingstoryapp.ui.main_activity.StoryPagingAdapter
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Requirement
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
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
    fun `Failed login because wrong Email or Password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com", password = "kamil"
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Failed login because of empty email or password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword("", "")
        Assert.assertTrue(
            actualResult is NetworkResponse.GenericException
        )
    }

    @Test
    fun `Successful login with correct Email and Password`() = runTest {
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
    fun `Failed get all stories with Location`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(token = "token_salah")
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successful get all stories with Location`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(Requirement.getToken())
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Failed Uploading file because of empty description`() = runTest {
        val actualResult = repository.uploadStory(
            photo = photo,
            description = "",
            token = Requirement.getToken()
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successful Upload Image without any location`() = runTest {
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
    fun `Paging3 Test`() = runTest {
        val actualResult = repository.getStoriesWithPagination(Requirement.getToken())
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main
        )
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(actualResult.getOrAwaitValue(), differ.snapshot())
    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

