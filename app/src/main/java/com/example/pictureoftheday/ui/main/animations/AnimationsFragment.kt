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
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView

import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentAnimationsBinding
import com.example.pictureoftheday.databinding.FragmentAnimationsEnlargeBinding

class AnimationsFragment : Fragment() {

    private var _binding: FragmentAnimationsEnlargeBinding? = null
    private val binding get() = _binding!!

    private var isExpanded = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentAnimationsEnlargeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.imageView.setOnClickListener {

            TransitionManager.beginDelayedTransition(
                binding.container, TransitionSet()
                    .addTransition(ChangeBounds())
                    .addTransition(ChangeImageTransform())
            )

            val params: ViewGroup.LayoutParams = binding.imageView.layoutParams
            params.height =
                if (isExpanded) ViewGroup.LayoutParams.WRAP_CONTENT
                else ViewGroup.LayoutParams.MATCH_PARENT
            binding.imageView.layoutParams = params
            binding.imageView.scaleType =
                if (isExpanded) ImageView.ScaleType.FIT_CENTER
                else ImageView.ScaleType.CENTER

            isExpanded = !isExpanded
        }
    }
}