package com.lelestacia.dicodingstoryapp.ui.main_activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.AsyncPagingDataDiffer
import androidx.paging.PagingData
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.data.model.mapper.DataMapper
import com.lelestacia.dicodingstoryapp.data.repository.MainDispatcherRule
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.data.repository.StoryPagingSourceTest
import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.Requirement
import kotlinx.coroutines.Dispatchers
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @get:Rule
    var dispatcher = MainDispatcherRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var repository: MainRepository
    private lateinit var fakeRepository: MainRepository
    private lateinit var apiService: DicodingAPI

    @Before
    fun setup(){
        repository = Module.getRepository()
        fakeRepository = Mockito.mock(MainRepository::class.java)
        mainViewModel = MainViewModel(fakeRepository)
        apiService = Module.getRetrofit()
    }

    @Test
    fun `Failed to get story list due to wrong token`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation("token_salah")
        `when`(fakeRepository.getAllStoriesWithLocation(null)).thenReturn(actualResult)
        mainViewModel.getAllStoriesWithLocation()
        Assert.assertEquals(mainViewModel.storiesWithLocation.getOrAwaitValue(), actualResult)
    }

    @Test
    fun `Managed to get the whole story with location`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(Requirement.getToken())
        `when`(fakeRepository.getAllStoriesWithLocation(null)).thenReturn(actualResult)
        mainViewModel.getAllStoriesWithLocation()
        Assert.assertEquals(mainViewModel.storiesWithLocation.getOrAwaitValue(), actualResult)
    }

    @Test
    fun `Successfully got story by page`() = runTest {
        val actualResult = apiService.getStoriesWithPage(
            token = Requirement.getToken(),
            page = 1,
            size = 10
        )
        val listStories = arrayListOf<LocalStory>()
        actualResult.listNetworkStory.forEach {
            listStories.add(DataMapper.mapStory(it))
        }
        val dummy: PagingData<LocalStory> = StoryPagingSourceTest.snapshot(listStories)
        val testingLiveData: MutableLiveData<PagingData<LocalStory>> = MutableLiveData()
        testingLiveData.value = dummy
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryPagingAdapter.DIFF_CALLBACK,
            updateCallback = Module.noopListUpdateCallback,
            mainDispatcher = Dispatchers.Main
        )
        `when`(fakeRepository.getStoriesWithPagination(null))
            .thenReturn(testingLiveData as LiveData<PagingData<LocalStory>>)
        Assert.assertNotNull(testingLiveData)
        differ.submitData(mainViewModel.getStoriesWithPage().getOrAwaitValue())
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(listStories.size, differ.snapshot().size)
        Assert.assertEquals(listStories, differ.snapshot())
        Assert.assertEquals(listStories[0], differ.snapshot()[0])
    }
}