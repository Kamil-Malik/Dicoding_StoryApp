package com.lelestacia.dicodingstoryapp.ui.register_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _registerInformation: MutableLiveData<NetworkResponse<AddStoryAndRegisterResponse>> = MutableLiveData()
    val registerInformation get() = _registerInformation

    fun signUpWithEmailAndPassword(username: String, email: String, password: String) {
        _registerInformation.postValue(NetworkResponse.Loading)
        viewModelScope.launch(Dispatchers.IO) {
            _registerInformation.postValue(repository.signUpUserWithEmailAndPassword(username, email, password))
        }
    }
}