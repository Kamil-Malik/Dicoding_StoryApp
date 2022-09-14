package com.lelestacia.dicodingstoryapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(binding.navHost.id) as NavHostFragment
        val navController = navHostFragment.navController
    }

    companion object {
        const val USER_PREF = "user_pref"
        const val USER_TOKEN = "user_token"
        const val USERNAME = "username"
        const val USER_ID = "user_id"
        const val STORY = "story"
        const val DARK_MODE = "dark_mode"
    }
}