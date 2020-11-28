package com.aldidwikip.mygithubuser.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.ui.follows.FollowsFragment

class SectionsPagerAdapter(private val mContext: Context, fm: FragmentManager) :
        FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    private val tabTitles = intArrayOf(R.string.tab_title_1, R.string.tab_title_2)
    var username: String? = null

    override fun getCount(): Int = 2

    override fun getItem(position: Int): Fragment {
        return FollowsFragment.newInstance(username, position)
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mContext.resources.getString(tabTitles[position])
    }
}