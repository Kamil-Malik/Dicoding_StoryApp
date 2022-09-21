package com.lelestacia.dicodingstoryapp.data.repository

import androidx.lifecycle.LiveData
import com.lelestacia.dicodingstoryapp.data.model.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
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

    suspend fun getAllStories(): NetworkResponse<GetStoriesResponse>

    suspend fun uploadStory(photo: File, description: String) : NetworkResponse<AddStoryAndRegisterResponse>

    fun isUpdated() : LiveData<Boolean>
}