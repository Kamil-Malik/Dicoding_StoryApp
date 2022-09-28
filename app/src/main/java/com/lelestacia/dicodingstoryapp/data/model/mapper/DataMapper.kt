package com.lelestacia.dicodingstoryapp.data.model.mapper

import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.data.model.network.NetworkStory

object DataMapper {

    fun mapStory(networkStory: NetworkStory) : LocalStory {
        return LocalStory(
            id = networkStory.id,
            name = networkStory.name,
            createdAt = networkStory.createdAt,
            photoUrl = networkStory.photoUrl,
            description = networkStory.description
        )
    }
}