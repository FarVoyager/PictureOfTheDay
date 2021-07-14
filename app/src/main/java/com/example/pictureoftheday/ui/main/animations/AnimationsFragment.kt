package com.example.pictureoftheday.ui.main.animations

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentAnimationsBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsEnlargeBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsPathTransitionsBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsShuffleBinding
import java.util.ArrayList

class AnimationsFragment : Fragment() {

    private var _binding: FragmentAnimationsShuffleBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimationsShuffleBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val titles: MutableList<String> = ArrayList()
        for (i in 0..4) {
            titles.add(String.format("Item %d", i + 1))
        }
        createViews(binding.transitionsContainer, titles)
        binding.button.setOnClickListener {
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, ChangeBounds())
            titles.shuffle()
            createViews(binding.transitionsContainer, titles)
        }
    }

    private fun createViews(layout: ViewGroup, titles: List<String>) {
        layout.removeAllViews()
        for (title in titles) {
            val newButton = Button(requireContext())
            newButton.text = title
            newButton.gravity = Gravity.CENTER_HORIZONTAL
            ViewCompat.setTransitionName(newButton, title)
            layout.addView(newButton)
        }
    }

}