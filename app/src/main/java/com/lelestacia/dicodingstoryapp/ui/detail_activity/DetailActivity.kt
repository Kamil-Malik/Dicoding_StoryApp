package com.lelestacia.dicodingstoryapp.ui.detail_activity

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lelestacia.dicodingstoryapp.data.model.network.NetworkStory
import com.lelestacia.dicodingstoryapp.databinding.ActivityDetailBinding
import com.lelestacia.dicodingstoryapp.utility.DateFormatter
import com.lelestacia.dicodingstoryapp.utility.Utility
import java.util.TimeZone

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    @RequiresApi(Build.VERSION_CODES.O)
    @Suppress("DEPRECATION")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myCustomToolbar)

        val networkStory = intent.getParcelableExtra<NetworkStory>(Utility.STORY)
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(networkStory!!.photoUrl)
                .fitCenter()
                .into(ivPhoto)
            tvTitle.text = networkStory.name
            tvTimestamp.text = DateFormatter.formatDate(networkStory.createdAt, TimeZone.getDefault().id)
            tvDeskripsi.text = networkStory.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}