package com.lelestacia.dicodingstoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.network.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.network.LoginResponse
import com.lelestacia.dicodingstoryapp.data.model.network.NetworkStory
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import java.io.File

interface MainRepository {
    suspend fun signUpUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): NetworkResponse<AddStoryAndRegisterResponse>

    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse>

    fun getStoriesWithPagination() : LiveData<PagingData<NetworkStory>>

    fun update()

    suspend fun getAllStories(): NetworkResponse<GetStoriesResponse>

    suspend fun uploadStory(photo: File, description: String) : NetworkResponse<AddStoryAndRegisterResponse>

    suspend fun uploadStory(photo: File, description: String, lat: Float, long: Float) : NetworkResponse<AddStoryAndRegisterResponse>

    fun isUpdated() : LiveData<Boolean>
}