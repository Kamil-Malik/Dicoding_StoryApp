package com.lelestacia.dicodingstoryapp.application

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import com.lelestacia.dicodingstoryapp.utility.Utility
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val sharedPref = applicationContext.getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
        val isDarkMode: Boolean = sharedPref.getBoolean(Utility.DARK_MODE, false)
        if(isDarkMode)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}