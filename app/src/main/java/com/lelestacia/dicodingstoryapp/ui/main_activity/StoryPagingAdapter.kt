package com.lelestacia.dicodingstoryapp.ui.main_activity

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lelestacia.dicodingstoryapp.data.model.local.LocalStory
import com.lelestacia.dicodingstoryapp.databinding.StoryItemBinding
import com.lelestacia.dicodingstoryapp.ui.detail_activity.DetailActivity
import com.lelestacia.dicodingstoryapp.utility.DateFormatter
import com.lelestacia.dicodingstoryapp.utility.Utility
import java.util.*

class StoryPagingAdapter :
    PagingDataAdapter<LocalStory, StoryPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.bind(item)
    }

    inner class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(item: LocalStory) {
            binding.apply {
                Glide.with(this.root.context)
                    .load(item.photoUrl)
                    .fitCenter()
                    .into(ivPhoto)
                tvTitle.text = item.name
                tvUploadeDate.text = DateFormatter.formatDate(item.createdAt, TimeZone.getDefault().id)
                this.root.setOnClickListener {
                    with(it.context) {
                        startActivity(
                            Intent(this, DetailActivity::class.java)
                                .putExtra(Utility.STORY, item),
                            ActivityOptionsCompat.makeSceneTransitionAnimation(
                                this as Activity,
                                Pair(binding.ivPhoto, "transition_photo"),
                                Pair(binding.tvTitle, "transition_title"),
                                Pair(binding.tvUploadeDate, "transition_description")
                            ).toBundle()
                        )
                    }
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<LocalStory>() {
            override fun areItemsTheSame(oldItem: LocalStory, newItem: LocalStory): Boolean =
                oldItem.id == newItem.id


            override fun areContentsTheSame(oldItem: LocalStory, newItem: LocalStory): Boolean =
                oldItem == newItem

        }
    }
}