package com.lelestacia.dicodingstoryapp.ui.stories_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoriesViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _stories: MutableLiveData<NetworkResponse<GetStoriesResponse>> =
        MutableLiveData(NetworkResponse.None)
    val stories: LiveData<NetworkResponse<GetStoriesResponse>> get() = _stories

    fun getAllStories() {
        viewModelScope.launch(Dispatchers.IO) {
            _stories.postValue(NetworkResponse.Loading)
            _stories.postValue(repository.getAllStories())
        }
    }

    fun isUpdated() = repository.isUpdated()
}