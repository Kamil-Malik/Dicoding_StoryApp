package com.lelestacia.dicodingstoryapp.ui.splash_activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.ui.login_activity.LoginActivity
import com.lelestacia.dicodingstoryapp.ui.main_activity.MainActivity
import com.lelestacia.dicodingstoryapp.utility.Utility
import kotlinx.coroutines.delay

@SuppressLint("CustomSplashScreen")
class CustomSplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val userToken = getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
            .getString(Utility.USER_TOKEN, "") ?: ""
        lifecycleScope.launchWhenCreated {
            delay(1500)
            if (userToken.isNotEmpty())
                startActivity(Intent(this@CustomSplashActivity, MainActivity::class.java))
                    .also { finish() }
            else
                startActivity(Intent(this@CustomSplashActivity, LoginActivity::class.java))
                    .also { finish() }
        }
    }
}