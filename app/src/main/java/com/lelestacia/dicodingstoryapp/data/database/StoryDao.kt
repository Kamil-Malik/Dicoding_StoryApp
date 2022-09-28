package com.lelestacia.dicodingstoryapp.data.database

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(story: List<LocalStory>)

    @Query("SELECT * FROM story")
    fun getAllStory() : PagingSource<Int, LocalStory>

    @Query("DELETE from story")
    suspend fun deleteAll()
}