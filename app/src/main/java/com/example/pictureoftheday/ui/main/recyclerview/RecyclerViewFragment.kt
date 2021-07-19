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
import com.example.pictureoftheday.databinding.*


class RecyclerViewFragment : Fragment() {

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

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
            Pair(Data("Mars", ""), false)
//            Data("Earth", "bist"),
//            Data("Mars", ""),
//            Data("Earth", "bast"),
//            Data("Earth", "bust"),
//            Data("Earth", "best"),
//            Data("Mars", null)
        )
        data.add(0, Pair(Data("Header"), false))

        val adapter = RecyclerViewAdapter(
            object : RecyclerViewAdapter.OnListItemClickListener {
                override fun onItemClick(data: Data) {
                    Toast.makeText(requireContext(), data.someText, Toast.LENGTH_SHORT).show()
                }
            },
            data
        )
        binding.lessonRecyclerView.adapter = adapter
        binding.recyclerFragmentFAB.setOnClickListener {
            adapter.appendItem()
        }
    }

    data class Data(
        val someText: String = "Text",
        val someDescription: String? = "Description"
    )

    class RecyclerViewAdapter(
        private var onListItemClickListener: OnListItemClickListener,
        private var data: MutableList<Pair<Data, Boolean>>
    ) : RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val itemBindingEarth =
                RecyclerItemEarthBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val itemBindingMars =
                RecyclerItemMarsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val itemBindingHeader =
                RecyclerViewHeaderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            return when (viewType) {
                TYPE_EARTH -> EarthViewHolder(itemBindingEarth)
                TYPE_MARS -> MarsViewHolder(itemBindingMars)
                else -> HeaderViewHolder(itemBindingHeader)
            }
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when (getItemViewType(position)) {
                TYPE_EARTH -> {
                    holder as EarthViewHolder
                    holder.bind(data[position])
                }
                TYPE_MARS -> {
                    holder as MarsViewHolder
                    holder.bind(data[position])
                }
                else -> {
                    holder as HeaderViewHolder
                    holder.bind(data[position])
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when {
                position == 0 -> TYPE_HEADER
                data[position].first.someDescription.isNullOrBlank() -> TYPE_MARS
                else -> TYPE_EARTH
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun appendItem() {
            data.add(generateItem())
            notifyItemInserted(itemCount - 1)
        }

        private fun generateItem(): Pair<Data, Boolean> {
            return Pair(Data("Mars", ""), false)
        }

        inner class EarthViewHolder(private val earthBinding: RecyclerItemEarthBinding) :
            BaseViewHolder(earthBinding.root) {
            override fun bind(data: Pair<Data, Boolean>) {
                if (layoutPosition != RecyclerView.NO_POSITION) {
                    earthBinding.earthImageView.setOnClickListener {
                        onListItemClickListener.onItemClick(data.first)
                        earthBinding.addItemImageView.setOnClickListener {
                            addItem()
                            earthBinding.removeItemImageView.setOnClickListener {
                                removeItem()
                            }
                        }
                    }
                }
            }

            private fun removeItem() {
                data.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
            }

            private fun addItem() {
                data.add(layoutPosition, generateItem())
                notifyItemInserted(layoutPosition)
            }

        }

        inner class MarsViewHolder(private val marsBinding: RecyclerItemMarsBinding) :
            BaseViewHolder(marsBinding.root) {
            override fun bind(data: Pair<Data, Boolean>) {
                marsBinding.marsImageView.setOnClickListener {
                    onListItemClickListener.onItemClick(data.first)
                }
                marsBinding.addItemImageView.setOnClickListener {
                    addItem()
                    marsBinding.removeItemImageView.setOnClickListener {
                        removeItem()
                    }
                }
                marsBinding.moveItemDown.setOnClickListener { moveItemDown() }
                marsBinding.moveItemUp.setOnClickListener { moveItemUp() }
                marsBinding.marsDescriptionTextView.visibility =
                    if (data.second) View.VISIBLE else View.GONE
                marsBinding.title.setOnClickListener { toggleText() }

            }

            private fun toggleText() {
                data[layoutPosition] = data[layoutPosition].let {
                    it.first to !it.second
                }
                notifyItemChanged(layoutPosition)
            }

            private fun moveItemUp() {
                layoutPosition.takeIf { it > 1 }?.also { currentPosition ->
                    data.removeAt(currentPosition).apply {
                        data.add(currentPosition - 1, this)
                    }
                    notifyItemMoved(currentPosition, currentPosition - 1)
                }
            }

            private fun moveItemDown() {
                layoutPosition.takeIf { it < data.size - 1}?.also { currentPosition ->
                    data.removeAt(currentPosition).apply {
                        data.add(currentPosition + 1, this)
                    }
                    notifyItemMoved(currentPosition, currentPosition + 1)
                }
            }

            private fun removeItem() {
                data.removeAt(layoutPosition)
                notifyItemRemoved(layoutPosition)
            }

            private fun addItem() {
                data.add(layoutPosition, generateItem())
                notifyItemInserted(layoutPosition)
            }
        }

        inner class HeaderViewHolder(private val headerBinding: RecyclerViewHeaderBinding) :
            BaseViewHolder(headerBinding.root) {
            override fun bind(data: Pair<Data, Boolean> ) {
                headerBinding.header.setOnClickListener {
                    onListItemClickListener.onItemClick(data.first)
                }
            }
        }

        companion object {
            private const val TYPE_EARTH = 0
            private const val TYPE_MARS = 1
            private const val TYPE_HEADER = 2
        }

        interface OnListItemClickListener {
            fun onItemClick(data: Data)
        }

        abstract class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            abstract fun bind(data: Pair<Data, Boolean> )
        }
    }
}