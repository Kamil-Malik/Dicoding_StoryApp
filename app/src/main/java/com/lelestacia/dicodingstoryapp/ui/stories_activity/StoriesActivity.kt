package com.lelestacia.dicodingstoryapp.ui.stories_activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.ActivityStoriesBinding
import com.lelestacia.dicodingstoryapp.ui.adapter.StoriesAdapter
import com.lelestacia.dicodingstoryapp.ui.add_story_activity.AddStoryActivity
import com.lelestacia.dicodingstoryapp.ui.settings_activity.SettingsActivity
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StoriesActivity : AppCompatActivity(), View.OnClickListener {

    private var _binding: ActivityStoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<StoriesViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityStoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter = StoriesAdapter()
        binding.apply {
            fabAddStory.setOnClickListener(this@StoriesActivity)
            rvStories.adapter = adapter
            rvStories.layoutManager = LinearLayoutManager(this@StoriesActivity)
            rvStories.setHasFixedSize(true)
        }

        viewModel.isUpdated().observe(this) {
            when(it) {
                true -> return@observe
                false -> viewModel.getAllStories()
            }
        }

        viewModel.stories.observe(this) { stories ->
            when (stories) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    this,
                    resources.getString(R.string.error_message, stories.code, stories.cause),
                    Toast.LENGTH_SHORT
                )
                    .show()
                NetworkResponse.Loading -> return@observe
                NetworkResponse.NetworkException -> Toast.makeText(
                    this,
                    resources.getString(R.string.error_network),
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

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.fabAddStory.id -> startActivity(Intent(this, AddStoryActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}