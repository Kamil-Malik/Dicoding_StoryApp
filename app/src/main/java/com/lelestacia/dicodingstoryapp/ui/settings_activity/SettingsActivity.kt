package com.lelestacia.dicodingstoryapp.ui.settings_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lelestacia.dicodingstoryapp.databinding.ActivitySettingsBinding
import com.lelestacia.dicodingstoryapp.ui.login_activity.LoginActivity
import com.lelestacia.dicodingstoryapp.utility.Utility

class SettingsActivity : AppCompatActivity() {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sharedPref = getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
        binding.switchDarkmode.isChecked = sharedPref.getBoolean(Utility.DARK_MODE, false)
        binding.switchDarkmode.setOnCheckedChangeListener { _, dark_mode ->
            if (dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            sharedPref.edit().putBoolean(Utility.DARK_MODE, dark_mode).apply()
        }

        binding.btnLogout.setOnClickListener {
            with(sharedPref.edit()) {
                this.putString(Utility.USER_TOKEN, "")
                this.putString(Utility.USER_ID, "")
                this.putString(Utility.USERNAME, "")
                apply()
            }
            startActivity(Intent(this, LoginActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}