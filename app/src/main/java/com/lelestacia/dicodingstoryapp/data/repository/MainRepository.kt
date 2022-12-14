package com.lelestacia.dicodingstoryapp.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.network.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.network.LoginResponse
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

    fun getStoriesWithPagination(token: String?) : LiveData<PagingData<LocalStory>>

    fun update()

    suspend fun getAllStoriesWithLocation(token: String?) : NetworkResponse<GetStoriesResponse>

    suspend fun uploadStory(photo: File, description: String, token: String?) : NetworkResponse<AddStoryAndRegisterResponse>

    suspend fun uploadStory(photo: File, description: String, lat: Float, long: Float, token: String?) : NetworkResponse<AddStoryAndRegisterResponse>

    fun isUpdated() : LiveData<Boolean>
}