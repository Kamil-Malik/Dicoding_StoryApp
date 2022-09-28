package com.lelestacia.dicodingstoryapp.data.model.network

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class GetStoriesResponse(

    @field:SerializedName("listStory")
    val listNetworkStory: List<NetworkStory>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) : Parcelable