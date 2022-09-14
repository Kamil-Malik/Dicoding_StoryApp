package com.lelestacia.dicodingstoryapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.dicodingstoryapp.data.model.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.data.repository.MainRepositoryImpl
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _loginInfo: MutableLiveData<NetworkResponse<LoginResponse>> = MutableLiveData()
    val loginInfo: LiveData<NetworkResponse<LoginResponse>> get() = _loginInfo

    private val _stories: MutableLiveData<NetworkResponse<GetStoriesResponse>> =
        MutableLiveData(NetworkResponse.None)
    val stories: LiveData<NetworkResponse<GetStoriesResponse>> get() = _stories

    private val _registerInformation: MutableLiveData<NetworkResponse<AddStoryAndRegisterResponse>> = MutableLiveData()
    val registerInformation get() = _registerInformation
    
    fun getAllStories() {
        viewModelScope.launch(Dispatchers.IO) {
            _stories.postValue(NetworkResponse.Loading)
            _stories.postValue(repository.getAllStories())
        }
    }

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

    fun signUpWithEmailAndPassword(username: String, email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _registerInformation.postValue(repository.signUpUserWithEmailAndPassword(username, email, password))
        }
    }
}