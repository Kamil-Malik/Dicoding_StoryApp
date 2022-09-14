package com.lelestacia.dicodingstoryapp.ui.stories_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.ui.settings_activity.SettingsActivity
import com.lelestacia.dicodingstoryapp.databinding.ActivityStoriesBinding
import com.lelestacia.dicodingstoryapp.ui.adapter.StoriesAdapter
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoriesActivity : AppCompatActivity() {

    private var _binding: ActivityStoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StoriesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        viewModel.stories.observe(this) { stories ->
            when (stories) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    this,
                    "Error ${stories.code} : ${stories.cause}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                NetworkResponse.Loading -> return@observe
                NetworkResponse.NetworkException -> Toast.makeText(
                    this,
                    "Silahkan periksa koneksi",
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResponse.Success -> adapter.submitList(stories.data.listStory)
                NetworkResponse.None -> viewModel.getAllStories()
            }
        }
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
}