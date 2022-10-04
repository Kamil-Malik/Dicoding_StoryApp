package com.lelestacia.dicodingstoryapp.ui.register_activity

import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class RegisterActivityTest {

    private lateinit var registerViewModel: RegisterViewModel

    @Before
    fun setup() {
        registerViewModel = RegisterViewModel(Module.getRepository())
    }

    @Test
    fun `Failed Registration`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 400, cause = "Bad Request")
        registerViewModel.signUpWithEmailAndPassword(
            username = "kamil",
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        val actualResult = registerViewModel.registerInformation.getOrAwaitValue()
        Assert.assertEquals(NetworkResponse.Loading, actualResult)
        if (actualResult is NetworkResponse.GenericException)
            Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Successful Registration`() = runTest {
        val randomString = UUID.randomUUID().toString()
        val expectedResult = false
        registerViewModel.signUpWithEmailAndPassword(
            username = "kamil",
            email = "$randomString@gmail.com",
            password = "kamilmalik"
        )
        val actualResult = registerViewModel.registerInformation.getOrAwaitValue()
        Assert.assertEquals(NetworkResponse.Loading, actualResult)
        if (actualResult is NetworkResponse.Success) {
            Assert.assertEquals(expectedResult, actualResult.data.error)
        }
    }
}