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
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentAnimationsBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsEnlargeBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsPathTransitionsBinding

class AnimationsFragment : Fragment() {

    private var _binding: FragmentAnimationsPathTransitionsBinding? = null
    private val binding get() = _binding!!

    private var toRightAnimation = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimationsPathTransitionsBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.button.setOnClickListener {
            val changeBounds = ChangeBounds()
            changeBounds.pathMotion = ArcMotion()
            changeBounds.duration = 500
            TransitionManager.beginDelayedTransition(binding.transitionsContainer, changeBounds)

            toRightAnimation = !toRightAnimation
            val params = binding.button.layoutParams as FrameLayout.LayoutParams
            params.gravity = if (toRightAnimation) Gravity.END or Gravity.BOTTOM
            else Gravity.START or Gravity.TOP
            binding.button.layoutParams = params


        }
    }
}