package com.lelestacia.dicodingstoryapp.data.repository

import android.content.Context
import com.google.gson.GsonBuilder
import com.lelestacia.dicodingstoryapp.data.api.DicodingApi
import com.lelestacia.dicodingstoryapp.data.model.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import com.lelestacia.dicodingstoryapp.utility.Utility
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class MainRepositoryImpl @Inject constructor(
    private val api: DicodingApi,
    private val context: Context
) : MainRepository {

    override suspend fun signUpUserWithEmailAndPassword(
        name: String,
        email: String,
        password: String
    ): NetworkResponse<AddStoryAndRegisterResponse>
    {
        return try {
            NetworkResponse.Success(api.signUpWithEmailAndPassword(name, email, password))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> NetworkResponse.NetworkException
                is HttpException -> {
                    NetworkResponse.GenericException(
                        t.code(),
                        t.message()
                    )
                }
                else -> NetworkResponse.GenericException(null, t.message)
            }
        }
    }

    override suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): NetworkResponse<LoginResponse> {
        return try {
            val response = api.signInWithEmailAndPassword(email, password)
            if (!response.error)
                context.getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
                    .edit().also {
                        it.putString(Utility.USER_TOKEN, response.loginResult.token)
                        it.putString(Utility.USER_ID, response.loginResult.userId)
                        it.putString(Utility.USERNAME, response.loginResult.name)
                        it.apply()
                    }
            NetworkResponse.Success(response)
        } catch (t: Throwable) {
            when (t) {
                is IOException -> NetworkResponse.NetworkException
                is HttpException -> {
                    NetworkResponse.GenericException(
                        t.code(),
                        t.message()
                    )
                }
                else -> NetworkResponse.GenericException(null, t.message)
            }
        }
    }

    override suspend fun getAllStories(): NetworkResponse<GetStoriesResponse> {
        return try {
            val token = "Bearer ${context.getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
                .getString(Utility.USER_TOKEN, "")}"
            NetworkResponse.Success(api.getStories(token))
        } catch (t: Throwable) {
            when (t) {
                is IOException -> NetworkResponse.NetworkException
                is HttpException -> {
                    NetworkResponse.GenericException(
                        t.code(),
                        t.message()
                    )
                }
                else -> NetworkResponse.GenericException(null, t.message)
            }
        }
    }

    private fun convertErrorBody(throwable: HttpException): Error? {
        return try {
            throwable.response()?.errorBody()?.source().let {
                val gson = GsonBuilder().create().getAdapter(Error::class.java)
                gson.fromJson(it.toString())
            }
        } catch (e: Exception) {
            null
        }
    }
}