package com.lelestacia.dicodingstoryapp.ui.add_story_activity


import android.net.Uri
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var photo: File
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        addStoryViewModel = AddStoryViewModel(Module.getRepository())
        val context = RuntimeEnvironment.getApplication()
        val uri: Uri =
            Uri.parse("android.resource://" + context.packageName.toString() + "/" + R.drawable.foto_twrp)
        photo = File(uri.path.toString())
        testDispatcher = StandardTestDispatcher()
    }

    @Test
    fun `Failed Uploading file because of empty description`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 400, cause = "Bad Request")
        addStoryViewModel.uploadStory(photo, "")
        val observer = Observer<NetworkResponse<AddStoryAndRegisterResponse>> {}
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