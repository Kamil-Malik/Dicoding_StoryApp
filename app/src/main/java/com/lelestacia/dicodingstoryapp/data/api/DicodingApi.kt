package com.lelestacia.dicodingstoryapp.data.api

import com.lelestacia.dicodingstoryapp.data.model.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
import retrofit2.Response
import retrofit2.http.*

interface DicodingApi {

    @FormUrlEncoded
    @POST("register")
    suspend fun signUpWithEmailAndPassword(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<AddStoryAndRegisterResponse>

    @FormUrlEncoded
    @POST("login")
    suspend fun signInWithEmailAndPassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<LoginResponse>

    @GET
    suspend fun getStories(
        @Header("Authorization") token: String
    ): Response<GetStoriesResponse>

    /*
        TODO :
            1. Cari cara input file dengan ekstensi image
            2. Cari cara include file tersebut sebagai parameter
            3. Cari cara membuat function untuk uplaod dengan body form-data
            4. Cari cara upload
     */
}