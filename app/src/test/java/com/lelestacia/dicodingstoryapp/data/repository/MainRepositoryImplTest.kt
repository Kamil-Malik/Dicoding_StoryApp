package com.lelestacia.dicodingstoryapp.data.repository

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.recyclerview.widget.ListUpdateCallback
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.database.StoryDatabase
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MainRepositoryImplTest {

    private lateinit var context: Context
    private lateinit var database: StoryDatabase
    private lateinit var apiService: DicodingAPI
    private lateinit var repository: MainRepositoryImpl
    private lateinit var photo: File
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTZ4dFRiWmt5Tk5tem5zN1AiLCJpYXQiOjE2NjI4MTA3MzZ9.DYfer_Yv5Lqs-UQMuMD2Vh-NimOhWQDjYdZLp-E0nXc"
    private val dispatcher = StandardTestDispatcher()

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
        val f = File("test-File")
        val inputStream: InputStream = context.resources.openRawResource(R.drawable.foto_twrp)
        val out: OutputStream = FileOutputStream(f)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) out.write(buf, 0, len)
        out.close()
        inputStream.close()
        photo = f
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        database.close()
        Dispatchers.resetMain()
    }

    @Test
    fun `Failed login because wrong Email or Password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamil"
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Failed login because of empty email or password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword("", "")
        Assert.assertTrue("This test should failed with Generic Exception",actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successful login with correct Email and Password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Failed Registration`() = runTest {
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successful Registration`() = runTest {
        val randomString = java.util.UUID.randomUUID().toString()
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "$randomString@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Successful get all stories with Location`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(token)
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Failed Uploading file because of empty description`() = runTest {
        val actualResult = repository.uploadStory(photo, "", token)
        Assert.assertTrue(actualResult is NetworkResponse.GenericException)
    }

    @Test
    fun `Successful Upload Image without any location`() = runTest {
        val actualResult = repository.uploadStory(photo, "Unit Test", token)
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

    @Test
    fun `Successful upload Image with location`() = runTest {
        val position = LatLng(-1.6128372919924492, 103.53365088692506)
        val actualResult = repository.uploadStory(
            photo,
            "Unit Test with Location",
            position.latitude.toFloat(),
            position.longitude.toFloat(),
            token
        )
        Assert.assertTrue(actualResult is NetworkResponse.Success)
    }

//    @Test
//    fun `Paging3 Test`() = runTest {
//        val actualResult = repository.getStoriesWithPagination(token).getOrAwaitValue()
//        val differ = AsyncPagingDataDiffer(
//            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
//            updateCallback = noopListUpdateCallback,
//            workerDispatcher = UnconfinedTestDispatcher(),
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

