package com.example.pictureoftheday.ui.main.viewPager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

private const val FRAGMENT_COUNT = 3

class ViewPagerAdapter(fragmentManager: FragmentManager) : FragmentStatePagerAdapter(fragmentManager) {

    override fun getCount(): Int {
        return FRAGMENT_COUNT
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> EarthFragment()
            1 -> MarsFragment()
            2 -> SystemFragment()
            else -> EarthFragment()
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Earth"
            1 -> "Mars"
            2 -> "System"
            else -> "Error"
        }
    }
}