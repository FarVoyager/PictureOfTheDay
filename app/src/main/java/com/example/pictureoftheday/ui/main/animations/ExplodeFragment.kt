package com.example.pictureoftheday.ui.main.animations

import android.graphics.Rect
import android.os.Build
import android.os.Bundle
import android.transition.*
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentExplodeBinding

class ExplodeFragment : Fragment() {

    private var _binding: FragmentExplodeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExplodeBinding.inflate(inflater,container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = Adapter()
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun explode(clickedView: View) {
        val recyclerView = binding.recyclerView
        val viewRect = Rect()
        clickedView.getGlobalVisibleRect(viewRect)
        val explode = Explode()
        explode.epicenterCallback = object : Transition.EpicenterCallback() {
            override fun onGetEpicenter(transition: Transition): Rect {
                return viewRect
            }
        }
        explode.excludeTarget(clickedView, true)
        val set = TransitionSet()
            .addTransition(explode)
            .addTransition(Fade().addTarget(clickedView))
            .addListener(@RequiresApi(Build.VERSION_CODES.O)
            object : TransitionListenerAdapter() {
                override fun onTransitionEnd(transition: Transition?) {
                    transition?.removeListener(this)
                }
            })

        TransitionManager.beginDelayedTransition(recyclerView, set)
        recyclerView.adapter = null
    }

    inner class Adapter : RecyclerView.Adapter<ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.fragment_explode_recycler_item,
                    parent,
                    false
                ) as View
            )
        }

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                explode(it)
            }
        }

        override fun getItemCount(): Int {
            return 32
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view)
}