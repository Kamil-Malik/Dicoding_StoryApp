package com.lelestacia.dicodingstoryapp.ui.login_activity

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.lelestacia.dicodingstoryapp.data.model.network.LoginResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.data.repository.getOrAwaitValue
import com.lelestacia.dicodingstoryapp.utility.Module
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.test.*
import org.junit.*
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
class LoginViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private lateinit var loginViewModel: LoginViewModel
    private lateinit var repository: MainRepository
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = Module.getRepository()
        loginViewModel = LoginViewModel(repository)
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Failed login because wrong Email or Password`() {
        val expectedResult = NetworkResponse.GenericException(code = 401, cause = "Unauthorized")
        val status = arrayListOf<NetworkResponse<LoginResponse>>()
        loginViewModel.loginInfo.observeForever {
            status.add(it)
        }
        loginViewModel.signInWithEmailAndPassword(
            email = "km8003296@gmail.com",
            password = "kamil"
        )
        dispatcher.isActive
        Assert.assertTrue(status[0] is NetworkResponse.Loading)
        Assert.assertTrue(status[1] is NetworkResponse.GenericException)
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