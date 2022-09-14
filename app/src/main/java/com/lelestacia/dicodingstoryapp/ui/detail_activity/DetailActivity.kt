package com.lelestacia.dicodingstoryapp.ui.detail_activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lelestacia.dicodingstoryapp.data.model.Story
import com.lelestacia.dicodingstoryapp.databinding.ActivityDetailBinding
import com.lelestacia.dicodingstoryapp.utility.Utility

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val story = intent.getParcelableExtra<Story>(Utility.STORY)
        Glide.with(this).load(story?.photoUrl).fitCenter().into(binding.ivPhoto)
        binding.tvTitle.text = story?.name
        val newTimeStamp = story?.createdAt?.replace("T", " - ")
        newTimeStamp?.removePrefix("Z")
        binding.tvTimestamp.text = newTimeStamp
        binding.tvDeskripsi.text = story?.description
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }
}