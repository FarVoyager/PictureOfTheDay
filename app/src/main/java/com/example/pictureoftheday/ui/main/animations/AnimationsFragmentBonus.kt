package com.example.pictureoftheday.ui.main.animations

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentAnimationsBonusStartBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsFabBinding

class AnimationsFragmentBonus : Fragment() {


    private var isShowing = false

    private var _binding: FragmentAnimationsBonusStartBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimationsBonusStartBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.backgroundImage.setOnClickListener {
            if (isShowing) hideComponents()
            else showComponents()
        }
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun showComponents() {
        isShowing = !isShowing

        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_animations_bonus_end)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
    }

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    private fun hideComponents() {
        isShowing = !isShowing

        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.fragment_animations_bonus_start)
        val transition = ChangeBounds()
        transition.interpolator = AnticipateOvershootInterpolator(1.0f)
        transition.duration = 1200
        TransitionManager.beginDelayedTransition(binding.constraintContainer, transition)
        constraintSet.applyTo(binding.constraintContainer)
    }

}