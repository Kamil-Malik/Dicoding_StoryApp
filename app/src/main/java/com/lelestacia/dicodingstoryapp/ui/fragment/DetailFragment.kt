package com.lelestacia.dicodingstoryapp.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.lelestacia.dicodingstoryapp.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private var _binding : FragmentDetailBinding? = null
    private val binding get() = _binding!!

    private val args : DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val story = args.story
        Glide.with(requireContext()).load(story.photoUrl).fitCenter().into(binding.ivPhoto)
        binding.tvTitle.text = story.name
        val newTimeStamp = story.createdAt.replace("T", " - ")
        binding.tvTimestamp.text = newTimeStamp
        binding.tvDeskripsi.text = story.description
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}