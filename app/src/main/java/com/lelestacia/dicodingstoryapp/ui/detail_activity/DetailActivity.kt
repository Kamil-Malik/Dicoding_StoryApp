package com.lelestacia.dicodingstoryapp.ui.detail_activity

import android.os.Build.VERSION
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.databinding.ActivityDetailBinding
import com.lelestacia.dicodingstoryapp.utility.DateFormatter
import com.lelestacia.dicodingstoryapp.utility.Utility
import java.util.*

class DetailActivity : AppCompatActivity() {

    private var _binding: ActivityDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.myCustomToolbar)

        val networkStory = if (VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(Utility.STORY, LocalStory::class.java)
        } else {
            intent.getParcelableExtra(Utility.STORY)
        }
        networkStory as LocalStory

        binding.apply {
            Glide.with(this@DetailActivity)
                .load(networkStory.photoUrl)
                .fitCenter()
                .into(ivPhoto)
            tvTitle.text = networkStory.name
            tvTimestamp.text =
                DateFormatter.formatDate(networkStory.createdAt, TimeZone.getDefault().id)
            tvDeskripsi.text = networkStory.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}