package com.lelestacia.dicodingstoryapp.ui.add_story_activity


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.android.gms.maps.model.LatLng
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Requirement
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.robolectric.RobolectricTestRunner
import java.io.File


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class AddStoryViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var addStoryViewModel: AddStoryViewModel
    private lateinit var photo: File
    private lateinit var repository: MainRepository
    private lateinit var fakeRepository: MainRepository

    @Before
    fun setup() {
        photo = Requirement.getPhoto()
        repository = Module.getRepository()
        fakeRepository = Mockito.mock(MainRepository::class.java)
        addStoryViewModel = AddStoryViewModel(fakeRepository)
    }

    @Test
    fun `Failed to upload due to empty description`() = runTest {
        val actualResult = repository.uploadStory(
            photo = photo,
            description = "",
            token = Requirement.getToken()
        )
        `when`(
            fakeRepository.uploadStory(
                photo = photo,
                description = "",
                token = null
            )
        ).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<AddStoryAndRegisterResponse>>()
        addStoryViewModel.uploadStatus.observeForever {
            listResult.add(it)
        }
        addStoryViewModel.uploadStory(photo, "")
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }

    @Test
    fun `Successfully upload image without location`() = runTest {
        val actualResult = repository.uploadStory(
            photo = photo,
            description = "Unit Test ViewModel",
            token = Requirement.getToken()
        )
        `when`(
            fakeRepository.uploadStory(
                photo = photo,
                description = "Unit Test ViewModel",
                token = null
            )
        ).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<AddStoryAndRegisterResponse>>()
        addStoryViewModel.uploadStatus.observeForever {
            listResult.add(it)
        }
        addStoryViewModel.uploadStory(photo, "Unit Test ViewModel")
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
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
        `when`(
            fakeRepository.uploadStory(
                photo = photo,
                description = "Unit Test with Location ViewModel",
                lat = position.latitude.toFloat(),
                long = position.longitude.toFloat(),
                token = null
            )
        ).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<AddStoryAndRegisterResponse>>()
        addStoryViewModel.uploadStatus.observeForever {
            listResult.add(it)
        }
        addStoryViewModel.uploadStory(
            photo,
            "Unit Test with Location ViewModel",
            position.latitude.toFloat(),
            position.longitude.toFloat()
        )
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }
}