package com.lelestacia.dicodingstoryapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.data.model.local.RemoteKeys

@Database(
    entities = [LocalStory::class, RemoteKeys::class],
    version = 1,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {

    abstract fun storyDao(): StoryDao
    abstract fun keyDao(): KeyDao
}