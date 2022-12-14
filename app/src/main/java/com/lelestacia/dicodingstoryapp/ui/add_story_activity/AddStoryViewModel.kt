package com.lelestacia.dicodingstoryapp.ui.add_story_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddStoryViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _uploadStatus : MutableLiveData<NetworkResponse<AddStoryAndRegisterResponse>> = MutableLiveData()
    val uploadStatus : LiveData<NetworkResponse<AddStoryAndRegisterResponse>> get() = _uploadStatus

    fun uploadStory(file: File, description: String) {
        _uploadStatus.value = NetworkResponse.Loading
        viewModelScope.launch{
            _uploadStatus.value = repository.uploadStory(file, description, null)
        }
    }

    fun uploadStory(file: File, description: String, lat: Float, lon: Float) {
        _uploadStatus.value = NetworkResponse.Loading
        viewModelScope.launch {
            _uploadStatus.value = repository.uploadStory(file, description, lat, lon, null)
        }
    }
}