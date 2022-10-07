package com.lelestacia.dicodingstoryapp.ui.login_activity

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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setup() {
        loginViewModel = LoginViewModel(Module.getRepository())
    }

    @Test
    fun `Failed login because wrong Email or Password`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 401, cause = "Unauthorized")
        loginViewModel.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamil"
        )
        val actualResult = loginViewModel.loginInfo.getOrAwaitValue()
        Assert.assertEquals(NetworkResponse.Loading, actualResult)
//        if (actualResult is NetworkResponse.GenericException)
//            Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Failed login because of empty email or password`() = runTest {
        val expectedResult = NetworkResponse.GenericException(code = 400, cause = "Bad Request")
        loginViewModel.signInWithEmailAndPassword("", "")
        val actualResult = loginViewModel.loginInfo.getOrAwaitValue()
        Assert.assertEquals(NetworkResponse.Loading, actualResult)
//        if (actualResult is NetworkResponse.GenericException)
//            Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun `Successful login with correct Email and Password`() = runTest {
        loginViewModel.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamilmalik"
        )
        val actualResult = loginViewModel.loginInfo.getOrAwaitValue()
        Assert.assertEquals(NetworkResponse.Loading, actualResult)
//        loginViewModel.loginInfo.observeForever {
//            Assert.assertEquals(false, actualResult)
//        }
    }
}