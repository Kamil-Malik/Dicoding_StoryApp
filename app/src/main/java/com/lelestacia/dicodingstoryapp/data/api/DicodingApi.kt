package com.lelestacia.dicodingstoryapp.data.api

import com.lelestacia.dicodingstoryapp.data.model.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DicodingApi {

    @FormUrlEncoded
    @POST("register")
    suspend fun signUpWithEmailAndPassword(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): AddStoryAndRegisterResponse

    @FormUrlEncoded
    @POST("login")
    suspend fun signInWithEmailAndPassword(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String
    ): GetStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part("file") file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): AddStoryAndRegisterResponse

    /*
        TODO :
            1. Cari cara input file dengan ekstensi image
            2. Cari cara include file tersebut sebagai parameter
            3. Cari cara membuat function untuk uplaod dengan body form-data
            4. Cari cara upload
     */
}