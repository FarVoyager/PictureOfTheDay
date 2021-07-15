package com.example.pictureoftheday.ui.main.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.transition.*
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView

import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.*
import java.util.ArrayList

class AnimationsFragment : Fragment() {

    private var isExpanded = false

    private var _binding: FragmentAnimationsFabBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimationsFabBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.scrollView.setOnScrollChangeListener { _, _, _, _, _ ->
            binding.header.isSelected = binding.scrollView.canScrollVertically(-1)
        }
        setFab()
    }

    private fun setFab() {
        setInitialState()
        binding.fab.setOnClickListener {
            if (isExpanded) {
                collapseFab()
            } else {
                expandFab()
            }
        }
    }

    private fun setInitialState() {
        binding.transparentBackground.alpha = 0f
        binding.optionOneContainer.apply {
            alpha = 0f
            isClickable = false
        }
        binding.optionTwoContainer.apply {
            alpha = 0f
            isClickable = false
        }
    }

    private fun expandFab() {
        isExpanded = !isExpanded
        ObjectAnimator.ofFloat(binding.plusImageview, "rotation", 0f, 135f).start()
        ObjectAnimator.ofFloat(binding.optionOneContainer, "translationY", -250f).start()
        ObjectAnimator.ofFloat(binding.optionTwoContainer, "translationY", -130f).start()

        binding.optionTwoContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionTwoContainer.isClickable = true
                    binding.optionTwoContainer.setOnClickListener {
                        Toast.makeText(requireContext(), "Option 2", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        binding.optionOneContainer.animate()
            .alpha(1f)
            .setDuration(300)
            .setListener(object :AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionOneContainer.isClickable = true
                    binding.optionOneContainer.setOnClickListener {
                        Toast.makeText(requireContext(), "Option 1", Toast.LENGTH_SHORT).show()
                    }
                }
            })

        binding.transparentBackground.animate()
            .alpha(0.9f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.transparentBackground.isClickable = true
                }
            })
    }

    private fun collapseFab() {
        isExpanded = !isExpanded
        ObjectAnimator.ofFloat(binding.plusImageview, "rotation",  -90f).start()
        ObjectAnimator.ofFloat(binding.optionOneContainer, "translationY", 0f).start()
        ObjectAnimator.ofFloat(binding.optionTwoContainer, "translationY", 0f).start()

        binding.optionTwoContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionTwoContainer.isClickable = false
                    binding.optionTwoContainer.setOnClickListener (null)
                }
            })

        binding.optionOneContainer.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object :AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.optionOneContainer.isClickable = false
                    binding.optionOneContainer.setOnClickListener (null)
                }
            })

        binding.transparentBackground.animate()
            .alpha(0f)
            .setDuration(300)
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    binding.transparentBackground.isClickable = false
                }
            })
    }

}