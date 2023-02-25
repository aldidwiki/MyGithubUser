package com.aldidwikip.mygithubuser.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aldidwikip.mygithubuser.ui.follows.FollowsFragment

class SectionsPagerAdapter(private val itemsCount: Int, fm: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fm, lifecycle) {
    var username: String? = null

    override fun getItemCount(): Int = itemsCount

    override fun createFragment(position: Int): Fragment {
        return FollowsFragment.newInstance(username, position)
    }
}