package com.lelestacia.dicodingstoryapp.data.api

import com.lelestacia.dicodingstoryapp.data.model.network.AddStoryAndRegisterResponse
import com.lelestacia.dicodingstoryapp.data.model.network.GetStoriesResponse
import com.lelestacia.dicodingstoryapp.data.model.network.LoginResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface DicodingAPI {

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

    @GET("stories")
    suspend fun getAllStoriesWithLocation(
        @Header("Authorization") token : String,
        @Query("location") location : Int = 1,
        @Query("sizes") sizes : Int = 100
    ): GetStoriesResponse

    @GET("stories")
    suspend fun getStoriesWithPage(
        @Header("Authorization") token : String,
        @Query("page") page : Int,
        @Query("size") size : Int
    ): GetStoriesResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String
    ): AddStoryAndRegisterResponse

    @Multipart
    @POST("stories")
    suspend fun addStory(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Header("Authorization") token: String,
        @Part("lat") lat : Float,
        @Part("lon") long: Float
    ): AddStoryAndRegisterResponse
}