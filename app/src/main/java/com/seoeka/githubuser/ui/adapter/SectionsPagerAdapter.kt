package com.seoeka.githubuser.ui.adapter

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.seoeka.githubuser.ui.view.fragment.TabFragment

class SectionsPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {
    var username: String = "eka"
    override fun createFragment(position: Int): Fragment {
        val fragment = TabFragment()
        fragment.arguments = Bundle().apply {
            putInt(TabFragment.ARG_POSITION, position + 1)
            putString(TabFragment.ARG_USERNAME, username)
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return 2
    }
}