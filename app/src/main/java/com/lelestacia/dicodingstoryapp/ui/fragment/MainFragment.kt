package com.lelestacia.dicodingstoryapp.ui.fragment

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.FragmentMainBinding
import com.lelestacia.dicodingstoryapp.ui.adapter.StoriesAdapter
import com.lelestacia.dicodingstoryapp.ui.viewmodel.MainViewModel
import com.lelestacia.dicodingstoryapp.utility.NetworkResponse

class MainFragment : Fragment(), View.OnClickListener, MenuProvider {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.show()
        binding.fabAddStory.setOnClickListener(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
        val adapter = StoriesAdapter()
        binding.rvStories.adapter = adapter
        binding.rvStories.layoutManager = LinearLayoutManager(context)
        viewModel.stories.observe(viewLifecycleOwner) { stories ->
            when (stories) {
                is NetworkResponse.GenericException -> Toast.makeText(
                    context,
                    "Error ${stories.code} : ${stories.cause}",
                    Toast.LENGTH_SHORT
                )
                    .show()
                NetworkResponse.Loading -> return@observe
                NetworkResponse.NetworkException -> Toast.makeText(
                    context,
                    "Silahkan periksa koneksi",
                    Toast.LENGTH_SHORT
                ).show()
                is NetworkResponse.Success -> adapter.submitList(stories.data.listStory)
                NetworkResponse.None -> viewModel.getAllStories()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.fabAddStory.id -> view?.findNavController()?.navigate(R.id.main_to_addstory)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.menuSetting -> view?.findNavController()?.navigate(R.id.main_to_settings)
        }
        return true
    }
}