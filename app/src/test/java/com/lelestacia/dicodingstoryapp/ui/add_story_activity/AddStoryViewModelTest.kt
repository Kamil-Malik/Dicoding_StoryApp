package com.lelestacia.dicodingstoryapp.ui.add_story_activity

import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import java.io.File

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddStoryViewModelTest {

    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var photo: File

    @Before
    fun setup() {
        addStoryViewModel = AddStoryViewModel(Module.getRepository())
        RuntimeEnvironment.getApplication()
//        val iniINPUt = Assert.assertEquals(context.resources.getDrawable(R.id.photo_twrp))
//        val input = Uri.parse(context.getFileStreamPath("foto_twrp.png").path).path
//        photo = File(input)
    }

    @Test
    fun `Failed Uploading file because of empty description`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 400, cause = "Bad Request")
        addStoryViewModel.uploadStory(photo, "")
        val actualResult = addStoryViewModel.uploadStatus.getOrAwaitValue()
        assertEquals(NetworkResponse.Loading, actualResult)
        if (actualResult is NetworkResponse.GenericException)
            assertEquals(expectedResult, actualResult)
    }
}