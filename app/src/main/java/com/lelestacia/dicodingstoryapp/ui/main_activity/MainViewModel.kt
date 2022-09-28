package com.lelestacia.dicodingstoryapp.ui.main_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.lelestacia.dicodingstoryapp.data.model.network.NetworkStory
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    repository: MainRepository
) : ViewModel() {

    val stories: LiveData<PagingData<NetworkStory>> =
        repository.getStoriesWithPagination().cachedIn(viewModelScope)

    val isUpdated = repository.isUpdated()
}