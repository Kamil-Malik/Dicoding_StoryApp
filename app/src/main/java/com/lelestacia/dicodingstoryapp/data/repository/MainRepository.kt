package com.lelestacia.dicodingstoryapp.data.repository

import com.lelestacia.dicodingstoryapp.data.model.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse

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
}