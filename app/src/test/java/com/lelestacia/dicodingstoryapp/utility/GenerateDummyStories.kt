package com.lelestacia.dicodingstoryapp.utility

import com.lelestacia.dicodingstoryapp.data.model.network.NetworkStory

object GenerateDummyStories {

    fun generateStories() : List<NetworkStory> {

        val stories = ArrayList<NetworkStory>()

        for (i in 0..5) {
            stories.add(
                NetworkStory(
                    id = "lelestacia",
                    name = "LeleStacia",
                    photoUrl = "google.com",
                    createdAt = "29 September 2022",
                    lat = 30.0,
                    lon = 20.0,
                    description = "Ini adalah deskripsi"
                )
            )
        }

        return stories
    }
}