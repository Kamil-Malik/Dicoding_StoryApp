package com.lelestacia.dicodingstoryapp.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.lelestacia.dicodingstoryapp.data.model.Story
import com.lelestacia.dicodingstoryapp.databinding.StoryItemBinding
import com.lelestacia.dicodingstoryapp.ui.detail_activity.DetailActivity
import com.lelestacia.dicodingstoryapp.ui.fragment.MainFragmentDirections
import com.lelestacia.dicodingstoryapp.utility.Utility

class StoriesAdapter : ListAdapter<Story, StoriesAdapter.ViewHolder>(StoriesComparator()) {

    inner class ViewHolder(private val binding: StoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Story) {
            Glide.with(binding.root.context).load(item.photoUrl).into(binding.ivPhoto)
            binding.tvTitle.text = item.name

            binding.root.setOnClickListener {
                with(itemView.context) {
                    startActivity(Intent(this, DetailActivity::class.java).putExtra(
                        Utility.STORY, item
                    ))
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.bind(item)
    }

    class StoriesComparator : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean =
            oldItem == newItem

    }
}