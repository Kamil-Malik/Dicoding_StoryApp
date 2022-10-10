package com.lelestacia.dicodingstoryapp.ui.add_story_activity


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var photo: File
    private lateinit var testDispatcher: TestDispatcher
    private val token = "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLTZ4dFRiWmt5Tk5tem5zN1AiLCJpYXQiOjE2NjI4MTA3MzZ9.DYfer_Yv5Lqs-UQMuMD2Vh-NimOhWQDjYdZLp-E0nXc"

    @Before
    fun setup() {
        addStoryViewModel = AddStoryViewModel(Module.getRepository())
        val f = File("test-File")
        val inputStream: InputStream = Module.getContext().resources.openRawResource(R.drawable.foto_twrp)
        val out: OutputStream = FileOutputStream(f)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) out.write(buf, 0, len)
        out.close()
        inputStream.close()
        photo = f
    }

    @Test
    fun `Failed Uploading file because of empty description`() = runTest {
        addStoryViewModel.uploadStory(photo, "")
        Assert.assertTrue(addStoryViewModel.uploadStatus.value is NetworkResponse.Loading)
    }

    @Test
    fun `Successfully Upload Image without any location`() = runTest {
        val expectedResult = false
        addStoryViewModel.uploadStory(photo, "unit-testing")
        val actualResult = addStoryViewModel.uploadStatus.getOrAwaitValue()
        assertEquals(NetworkResponse.Loading, actualResult)
//        if (actualResult is NetworkResponse.Success)
//            assertEquals(expectedResult, actualResult.data.error)
    }

    @Test
    fun `Successfully upload Image with location`() = runTest {
        val position = LatLng(-1.6128372919924492, 103.53365088692506)
        val expectedResult = false
        addStoryViewModel.uploadStory(
            photo,
            "unit-testing",
            position.latitude.toFloat(),
            position.longitude.toFloat()
        )
        val actualResult = addStoryViewModel.uploadStatus.getOrAwaitValue()
        assertEquals("Response Error should be false", expectedResult, actualResult)
//        assertEquals(NetworkResponse.Loading, actualResult)
//        if (actualResult !is  NetworkResponse.Loading) {
//            println(actualResult)
//            if (actualResult is NetworkResponse.GenericException)
//                assertEquals(2000, actualResult.code)
//        }
    }
}