package com.lelestacia.dicodingstoryapp.di

import android.content.Context
import com.lelestacia.dicodingstoryapp.BuildConfig
import com.lelestacia.dicodingstoryapp.data.api.DicodingApi
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
    fun provideDicodingAPi(client: OkHttpClient): DicodingApi =
        Retrofit.Builder().baseUrl("https://story-api.dicoding.dev/v1/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build().create(DicodingApi::class.java)

    @Provides
    @Singleton
    fun provideMainRepository(
        api: DicodingApi,
        @ApplicationContext context: Context
    ): MainRepository = MainRepositoryImpl(api, context)
}

