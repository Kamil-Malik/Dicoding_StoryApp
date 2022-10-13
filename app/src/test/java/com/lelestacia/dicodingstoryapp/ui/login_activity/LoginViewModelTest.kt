package com.lelestacia.dicodingstoryapp.ui.login_activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lelestacia.dicodingstoryapp.data.model.network.LoginResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainDispatcherRule
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val dispatch = MainDispatcherRule()

    private lateinit var repository: MainRepository
    private lateinit var fakeRepository: MainRepository
    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        repository = Module.getRepository()
        fakeRepository = Mockito.mock(MainRepository::class.java)
        loginViewModel = LoginViewModel(fakeRepository)
    }

    @Test
    fun `Login failed due to wrong email or password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamil"
        )
        `when`(
            fakeRepository.signInWithEmailAndPassword(
                email = "km8003296@gmail.com",
                password = "kamil"
            )
        ).thenReturn(actualResult)
        val listResult =
            arrayListOf<NetworkResponse<LoginResponse>>()
        loginViewModel.loginInfo.observeForever {
            listResult.add(it)
        }
        loginViewModel.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamil"
        )
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }

    @Test
    fun `Login failed due to empty email or password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword("", "")
        `when`(fakeRepository.signInWithEmailAndPassword("", "")).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<LoginResponse>>()
        loginViewModel.loginInfo.observeForever {
            listResult.add(it)
        }
        loginViewModel.signInWithEmailAndPassword("", "")
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }

    @Test
    fun `Successfully logged in with the correct email and password`() = runTest {
        val actualResult = repository.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        `when`(
            fakeRepository.signInWithEmailAndPassword(
                email = "km8003296@gmail.com",
                password = "kamilmalik"
            )
        ).thenReturn(actualResult)
        val listResult = arrayListOf<NetworkResponse<LoginResponse>>()
        loginViewModel.loginInfo.observeForever {
            listResult.add(it)
        }
        loginViewModel.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        Assert.assertEquals(listResult[0], NetworkResponse.Loading)
        Assert.assertEquals(listResult[1], actualResult)
    }
}