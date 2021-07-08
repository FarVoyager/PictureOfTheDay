package com.example.pictureoftheday.ui.main.viewPager

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentMainViewPagerBinding

class MainViewPagerFragment : Fragment() {

    private var _binding: FragmentMainViewPagerBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainViewPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = ViewPagerAdapter(childFragmentManager)
        binding.indicator.setViewPager(binding.viewPager)

        binding.tabLayout.apply {
            setupWithViewPager(binding.viewPager)
            getTabAt(0)?.setIcon(R.drawable.ic_earth)
            getTabAt(1)?.setIcon(R.drawable.ic_mars)
            getTabAt(2)?.setIcon(R.drawable.ic_system)

        }

    }
}