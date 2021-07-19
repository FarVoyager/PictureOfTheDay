package com.example.pictureoftheday.ui.main.recyclerview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.databinding.FragmentRecyclerViewBinding
import com.example.pictureoftheday.databinding.RecyclerItemEarthBinding
import com.example.pictureoftheday.databinding.RecyclerItemMarsBinding


class RecyclerViewFragment : Fragment() {

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentRecyclerViewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val data = arrayListOf(
            Data("Earth", "bost"),
            Data("Earth", "bist"),
            Data("Mars", ""),
            Data("Earth", "bast"),
            Data("Earth", "bust"),
            Data("Earth", "best"),
            Data("Mars", null)
        )
        binding.lessonRecyclerView.adapter = RecyclerViewAdapter(
            object : RecyclerViewAdapter.OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    Toast.makeText(requireContext(), data.someText, Toast.LENGTH_SHORT).show()
                }
            },
            data
        )
    }

    data class Data(
        val someText: String = "Text",
        val someDescription: String? = "Description"
    )


    class RecyclerViewAdapter(
        private var onListItemClickListener: OnListItemClickListener,
        private var data: List<Data>
    ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
//            val inflater = LayoutInflater.from(parent.context)
            val itemBindingEarth =
                RecyclerItemEarthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val itemBindingMars =
                RecyclerItemMarsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return if (viewType == TYPE_EARTH) {
                EarthViewHolder(
                    itemBindingEarth
                )
            } else {
                MarsViewHolder(
                    itemBindingMars
                )
            }
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if (getItemViewType(position) == TYPE_EARTH) {
                holder as EarthViewHolder
                holder.bind(data[position])
            } else {
                holder as MarsViewHolder
                holder.bind(data[position])
            }
        }

        override fun getItemViewType(position: Int): Int {
            return if (data[position].someDescription.isNullOrBlank()) TYPE_MARS else TYPE_EARTH
        }

        override fun getItemCount(): Int {
            return data.size
        }

        inner class EarthViewHolder(private val earthBinding: RecyclerItemEarthBinding) :
            RecyclerView.ViewHolder(earthBinding.root) {
            fun bind(data: Data) {
                if (layoutPosition != RecyclerView.NO_POSITION) {
                    earthBinding.descriptionTextView
                        .text = data.someDescription
                    earthBinding.wikiImageView.setOnClickListener {
                        onListItemClickListener.onItemClick(data)
                    }
                }
            }
        }

        inner class MarsViewHolder(private val marsBinding: RecyclerItemMarsBinding) :
            RecyclerView.ViewHolder(marsBinding.root) {
            fun bind(data: Data) {
                marsBinding.marsImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(data)
                }
            }
        }


        companion object {
            private const val TYPE_EARTH = 0
            private const val TYPE_MARS = 1
        }

        interface OnListItemClickListener {
            fun onItemClick(data: Data)
        }

    }

}