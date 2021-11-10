package com.example.pictureoftheday.ui.main.utils

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentLayoutsBinding
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.google.android.material.behavior.SwipeDismissBehavior

class LayoutsFragment : Fragment() {

    private var _binding: FragmentLayoutsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLayoutsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LayoutsFragment()
    }
}