package com.lelestacia.dicodingstoryapp.utility

import android.content.Context
import androidx.room.Room
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.database.StoryDatabase
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.data.repository.MainRepositoryImpl
import org.robolectric.RuntimeEnvironment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Module {

    fun getDatabase(): StoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            context = RuntimeEnvironment.getApplication(),
            klass = StoryDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
    }

    fun getContext(): Context {
        return RuntimeEnvironment.getApplication()
    }

    fun getRetrofit(): DicodingAPI {
        return Retrofit
            .Builder()
            .baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(DicodingAPI::class.java)
    }

    fun getRepository() : MainRepository {
        return MainRepositoryImpl(
            getRetrofit(),
            getContext(),
            getDatabase()
        )
    }
}