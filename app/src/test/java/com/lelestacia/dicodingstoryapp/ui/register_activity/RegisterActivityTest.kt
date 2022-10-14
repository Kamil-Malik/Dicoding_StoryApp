package com.lelestacia.dicodingstoryapp.ui.register_activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
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
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RegisterActivityTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    private lateinit var registerViewModel: RegisterViewModel
    private lateinit var repository: MainRepository
    private lateinit var fakeRepository: MainRepository

    @Before
    fun setup() {
        fakeRepository = Mockito.mock(MainRepository::class.java)
        repository = Module.getRepository()
        registerViewModel = RegisterViewModel(fakeRepository)
    }

    @Test
    fun `Failed Registration`() = runTest {
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        `when`(
            fakeRepository.signUpUserWithEmailAndPassword(
                name = "kamil",
                email = "km8003296@gmail.com",
                password = "kamilmalik"
            )
        ).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<AddStoryAndRegisterResponse>>()
        registerViewModel.registerInformation.observeForever {
            listResult.add(it)
        }
        registerViewModel.signUpWithEmailAndPassword(
            username = "kamil",
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }

    @Test
    fun `Successful Registration`() = runTest {
        val randomString = UUID.randomUUID().toString()
        val actualResult = repository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "$randomString@gmail.com",
            password = "kamilmalik"
        )
        `when`(fakeRepository.signUpUserWithEmailAndPassword(
            name = "kamil",
            email = "$randomString@gmail.com",
            password = "kamilmalik"
        )).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<AddStoryAndRegisterResponse>>()
        registerViewModel.registerInformation.observeForever {
            listResult.add(it)
        }
        registerViewModel.signUpWithEmailAndPassword(
            username = "kamil",
            email = "$randomString@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }
}