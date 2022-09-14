package com.lelestacia.dicodingstoryapp.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPref = applicationContext.getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        val isDarkMode: Boolean = sharedPref.getBoolean(DARK_MODE, false)
        if(isDarkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    companion object {
        private const val DARK_MODE = "dark_mode"
        const val USER_PREF = "user_pref"
        const val USER_TOKEN = "user_token"
    }
}