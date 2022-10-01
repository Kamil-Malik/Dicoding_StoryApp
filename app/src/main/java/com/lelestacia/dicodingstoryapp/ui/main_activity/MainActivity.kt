package com.lelestacia.dicodingstoryapp.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.ActivityMainBinding
import com.lelestacia.dicodingstoryapp.ui.settings_activity.SettingsActivity
import com.lelestacia.dicodingstoryapp.utility.Utility
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var _binding : ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainPagerAdapter = MainPagerAdapter(this)
        val viewPager = binding.viewPager2
        viewPager.isUserInputEnabled = false
        viewPager.adapter = mainPagerAdapter
        val tabs: TabLayout = binding.tabLayoutMain
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(Utility.TAB_TITLES[position])
        }.attach()
        setSupportActionBar(binding.myCustomToolbar)
        supportActionBar?.elevation = 0f
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSetting -> startActivity(Intent(this, SettingsActivity::class.java))
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}