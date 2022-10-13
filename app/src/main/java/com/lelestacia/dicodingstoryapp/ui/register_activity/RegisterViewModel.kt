package com.lelestacia.dicodingstoryapp.ui.register_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _registerInformation: MutableLiveData<NetworkResponse<AddStoryAndRegisterResponse>> =
        MutableLiveData()
    val registerInformation get() = _registerInformation

    fun signUpWithEmailAndPassword(username: String, email: String, password: String) {
        _registerInformation.value = NetworkResponse.Loading
        viewModelScope.launch {
            _registerInformation.value =
                repository.signUpUserWithEmailAndPassword(username, email, password)
//            _registerInformation.postValue(repository.signUpUserWithEmailAndPassword(username, email, password))
        }
    }
}