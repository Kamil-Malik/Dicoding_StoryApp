package com.lelestacia.dicodingstoryapp.ui.main_activity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.dicodingstoryapp.databinding.LoadingFooterBinding

class LoadingStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val binding = LoadingFooterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(binding, retry)
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class LoadingStateViewHolder(private val binding: LoadingFooterBinding, retry: () -> Unit)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            when(loadState) {
                is LoadState.NotLoading -> {
                    binding.root.visibility = View.GONE
                }
                LoadState.Loading -> {
                    binding.apply {
                        btnRetry.visibility = View.GONE
                        circleLoading.visibility = View.VISIBLE
                        tvTextLoading.visibility = View.VISIBLE
                    }
                }
                is LoadState.Error -> {
                    binding.apply {
                        btnRetry.visibility = View.VISIBLE
                        circleLoading.visibility = View.GONE
                        tvTextLoading.visibility = View.GONE
                    }
                }
            }
            binding.btnRetry.setOnClickListener { retry.invoke() }
        }
    }
}