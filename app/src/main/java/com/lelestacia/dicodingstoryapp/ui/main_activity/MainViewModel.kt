package com.lelestacia.dicodingstoryapp.ui.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.data.model.network.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: MainRepository
) : ViewModel() {

    private val _storiesWithLocation: MutableLiveData<NetworkResponse<GetStoriesResponse>> = MutableLiveData()
    val storiesWithLocation: LiveData<NetworkResponse<GetStoriesResponse>> get() = _storiesWithLocation

    init {
        getAllStoriesWithLocation()
    }

    fun getAllStoriesWithLocation() {
        viewModelScope.launch {
            _storiesWithLocation.value = repository.getAllStoriesWithLocation(null)
        }
    }

    val stories: LiveData<PagingData<LocalStory>> =
        repository.getStoriesWithPagination(null)

    val isUpdated = repository.isUpdated()
}