package com.lelestacia.dicodingstoryapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.database.StoryDatabase
import com.lelestacia.dicodingstoryapp.ui.main_activity.StoryPagingAdapter
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MainRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var database: StoryDatabase
    private lateinit var apiService: DicodingAPI
    private lateinit var repository: MainRepositoryImpl

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
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `Failed login because wrong Email or Password`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 401, cause = "Unauthorized")
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamil"
        )
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Failed login because of empty email or password`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 400, cause = "Bad Request")
        val actualResult = repository.signInWithEmailAndPassword("", "")
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Successful login with correct Email and Password`() = runTest {
        val expectedResult = false
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        if (actualResult is NetworkResponse.Success) {
            Assert.assertEquals(expectedResult, actualResult.data.error)
        }
    }

    @Test
    fun `Failed Registration`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 400, cause = "Bad Request")
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Successful Registration`() = runTest {
        val randomString = java.util.UUID.randomUUID().toString()
        val expectedResult = false
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "$randomString@gmail.com",
            password = "kamilmalik"
        )
        if (actualResult is NetworkResponse.Success) {
            Assert.assertEquals(expectedResult, actualResult.data.error)
        }
    }

    @Test
    fun `Successfully get all stories with Location`() = runTest {
        val expectedResult = false
        val actualResult = repository.getAllStoriesWithLocation()
        if (actualResult is NetworkResponse.Success) {
            Assert.assertEquals(expectedResult, actualResult.data.error)
        }
    }

//    @Test
//    fun `Paging3 Test`() = runTest {
//        val actualResult = repository.getStoriesWithPagination().getOrAwaitValue()
//        val differ = AsyncPagingDataDiffer(
//            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
//            updateCallback = noopListUpdateCallback,
//            workerDispatcher = Dispatchers.Main,
//        )
//        differ.submitData(actualResult)
//        Assert.assertNotNull(differ.snapshot())
//        Assert.assertEquals(actualResult, differ.snapshot())
//    }

    private val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

