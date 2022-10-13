package com.lelestacia.dicodingstoryapp.ui.main_activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class MainViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var mainViewModel: MainViewModel
    private lateinit var repository: MainRepository
    private lateinit var fakeRepository: MainRepository

    @Before
    fun setup(){
        repository = Module.getRepository()
        fakeRepository = Mockito.mock(MainRepository::class.java)
        mainViewModel = MainViewModel(fakeRepository)
    }

    @Test
    fun `Failed to get all stories with location due to invalid token`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation("token_salah")
        `when`(fakeRepository.getAllStoriesWithLocation(null)).thenReturn(actualResult)
        mainViewModel.getAllStoriesWithLocation()
        Assert.assertEquals(mainViewModel.storiesWithLocation.getOrAwaitValue(), actualResult)
    }

    @Test
    fun `Success to get all stories with location`() = runTest {
        val actualResult = repository.getAllStoriesWithLocation(Requirement.getToken())
        `when`(fakeRepository.getAllStoriesWithLocation(null)).thenReturn(actualResult)
        mainViewModel.getAllStoriesWithLocation()
        Assert.assertEquals(mainViewModel.storiesWithLocation.getOrAwaitValue(), actualResult)
    }
}