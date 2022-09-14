package com.lelestacia.dicodingstoryapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.lelestacia.dicodingstoryapp.application.MyApp
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.FragmentSplashBinding
import kotlinx.coroutines.delay


class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        (activity as AppCompatActivity).supportActionBar?.hide()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref =
            requireActivity().getSharedPreferences(MyApp.USER_PREF, Context.MODE_PRIVATE)
        val token = sharedPref.getString(MyApp.USER_TOKEN, "") as String
        lifecycleScope.launchWhenCreated {
            delay(2000)
            if (token.isEmpty())
                view.findNavController().navigate(R.id.splash_to_login)
            else
                view.findNavController().navigate(R.id.splash_to_main)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}