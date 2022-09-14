package com.lelestacia.dicodingstoryapp.ui.login_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _loginInfo: MutableLiveData<NetworkResponse<LoginResponse>> = MutableLiveData()
    val loginInfo: LiveData<NetworkResponse<LoginResponse>> get() = _loginInfo

    fun signInWithEmailAndPassword(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _loginInfo.postValue(
                repository.signInWithEmailAndPassword(
                    email = email,
                    password = password
                )
            )
        }
    }
}