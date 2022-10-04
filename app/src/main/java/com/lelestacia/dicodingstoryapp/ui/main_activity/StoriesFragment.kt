package com.lelestacia.dicodingstoryapp.ui.main_activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.dicodingstoryapp.databinding.FragmentStoriesBinding
import com.lelestacia.dicodingstoryapp.ui.add_story_activity.AddStoryActivity

class StoriesFragment : Fragment(), View.OnClickListener {

    private var _binding : FragmentStoriesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = StoryPagingAdapter()
        binding.apply {
            rvStories.adapter = adapter.withLoadStateFooter(
                footer = LoadingStateAdapter {
                    adapter.retry()
                }
            )
            rvStories.layoutManager = LinearLayoutManager(requireContext())
            fabAddStory.setOnClickListener(this@StoriesFragment)
        }

        viewModel.stories.observe(viewLifecycleOwner){
            adapter.submitData(lifecycle, it)
        }

        viewModel.isUpdated.observe(viewLifecycleOwner) {
            when(it) {
                true -> return@observe
                false -> adapter.refresh()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id){
            binding.fabAddStory.id -> startActivity(Intent(requireContext(), AddStoryActivity::class.java))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}