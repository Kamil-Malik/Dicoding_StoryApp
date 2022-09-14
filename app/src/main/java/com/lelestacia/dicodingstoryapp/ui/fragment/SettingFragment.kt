package com.lelestacia.dicodingstoryapp.ui.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.lelestacia.dicodingstoryapp.R
import com.lelestacia.dicodingstoryapp.databinding.FragmentSettingBinding
import com.lelestacia.dicodingstoryapp.utility.Utility

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPref =
            requireActivity().getSharedPreferences(Utility.USER_PREF, Context.MODE_PRIVATE)
        binding.switchDarkmode.isChecked = sharedPref.getBoolean(Utility.DARK_MODE, false)
        binding.switchDarkmode.setOnCheckedChangeListener { _, dark_mode ->
            if (dark_mode)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            sharedPref.edit().putBoolean(Utility.DARK_MODE, dark_mode).apply()
        }

        binding.btnLogout.setOnClickListener {
            with(sharedPref.edit()) {
                this.putString(Utility.USER_TOKEN, "")
                this.putString(Utility.USER_ID, "")
                this.putString(Utility.USERNAME, "")
                apply()
            }
            view.findNavController().navigate(R.id.logout)
        }
    }
}