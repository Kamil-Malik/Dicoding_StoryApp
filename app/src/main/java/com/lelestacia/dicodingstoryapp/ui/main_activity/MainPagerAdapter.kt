package com.lelestacia.dicodingstoryapp.ui.main_activity

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class MainPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    override fun createFragment(position: Int): Fragment {
        var fragment : Fragment? = null
        when(position) {
            0 -> fragment = MapsFragment()
            1 -> fragment = StoriesFragment()
        }
        return fragment as Fragment
    }

    override fun getItemCount(): Int = 2
}