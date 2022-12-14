package com.lelestacia.dicodingstoryapp.di

import android.content.Context
import androidx.room.Room
import androidx.viewbinding.BuildConfig
import com.lelestacia.dicodingstoryapp.data.api.DicodingAPI
import com.lelestacia.dicodingstoryapp.data.database.StoryDatabase
import com.lelestacia.dicodingstoryapp.data.repository.MainRepository
import com.lelestacia.dicodingstoryapp.data.repository.MainRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return if (BuildConfig.DEBUG) {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .build()
        } else {
            OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE))
                .build()
        }
    }

    @Provides
    @Singleton
    fun provideDicodingAPi(client: OkHttpClient): DicodingAPI =
        Retrofit.Builder().baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(DicodingAPI::class.java)


    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context) : StoryDatabase {
        return Room.databaseBuilder(
            context,
            StoryDatabase::class.java,
            "story_db"
        ).build()
    }


    @Provides
    @Singleton
    fun provideMainRepository(
        api: DicodingAPI,
        @ApplicationContext context: Context,
        storyDatabase: StoryDatabase
    ): MainRepository = MainRepositoryImpl(api, context, storyDatabase)
}

