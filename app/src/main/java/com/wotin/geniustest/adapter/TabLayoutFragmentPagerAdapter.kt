package com.wotin.geniustest.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.wotin.geniustest.fragment.GeniusTestFragment
import com.wotin.geniustest.fragment.PracticeFragment
import com.wotin.geniustest.fragment.RankingFragment

class TabLayoutFragmentPagerAdapter(fm : FragmentManager) : FragmentPagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        when(position) {
            0 -> return PracticeFragment()
            1 -> return GeniusTestFragment()
            2 -> return RankingFragment()
            else -> return PracticeFragment()
        }
    }



    override fun getCount(): Int = 3
}