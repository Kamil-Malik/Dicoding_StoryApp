package com.lelestacia.dicodingstoryapp.ui.settings_activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.lelestacia.dicodingstoryapp.databinding.ActivitySettingsBinding
import com.lelestacia.dicodingstoryapp.ui.login_activity.LoginActivity
import com.lelestacia.dicodingstoryapp.utility.Utility

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivitySettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myCustomToolbar)

        val sharedPref = getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)

        binding.apply {
            switchDarkmode.isChecked = sharedPref.getBoolean(Utility.DARK_MODE, false)
            switchDarkmode.setOnCheckedChangeListener { _, dark_mode ->
                if (dark_mode)
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
                sharedPref.edit().putBoolean(Utility.DARK_MODE, dark_mode).apply()
            }
            btnLogout.setOnClickListener(this@SettingsActivity)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnLogout.id -> {
                val sharedPref = getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
                with(sharedPref.edit()) {
                    putString(Utility.USER_TOKEN, "")
                    putString(Utility.USER_ID, "")
                    putString(Utility.USERNAME, "")
                    apply()
                }
                startActivity(
                    Intent(this, LoginActivity::class.java).apply {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                )
                finish()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}