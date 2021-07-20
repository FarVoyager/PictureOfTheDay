package com.example.pictureoftheday.ui.main.recyclerview

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.MotionEventCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.*


class RecyclerViewFragment : Fragment() {

    private var _binding: FragmentRecyclerViewBinding? = null
    private val binding get() = _binding!!

    lateinit var itemTouchHelper: ItemTouchHelper

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
            data,
            object : RecyclerViewAdapter.OnStartDragListener {
                override fun onStartDrag(viewHolder: RecyclerView.ViewHolder) {
                    itemTouchHelper.startDrag(viewHolder)
                }
            }
        )
        binding.lessonRecyclerView.adapter = adapter
        binding.recyclerFragmentFAB.setOnClickListener {
            adapter.appendItem()
        }
        itemTouchHelper = ItemTouchHelper(RecyclerViewAdapter.ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.lessonRecyclerView)

    }

    data class Data(
        val someText: String = "Text",
        val someDescription: String? = "Description"
    )

    class RecyclerViewAdapter(
        private var onListItemClickListener: OnListItemClickListener,
        private var data: MutableList<Pair<Data, Boolean>>,
        private val dragListener: OnStartDragListener
    ) : RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {

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
            BaseViewHolder(marsBinding.root), ItemTouchHelperViewHolder {
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

                marsBinding.dragHandleImageView.setOnTouchListener { v, event ->
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }

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

            override fun onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY)
            }

            override fun onItemClear() {
                itemView.setBackgroundColor(0)
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


        class ItemTouchHelperCallback(private val adapter: RecyclerViewAdapter) :
                ItemTouchHelper.Callback() {

            override fun isLongPressDragEnabled(): Boolean {
                return true
            }

            override fun isItemViewSwipeEnabled(): Boolean {
                return true
            }

            override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                adapter.onItemMove(viewHolder.adapterPosition, target.adapterPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.onItemDismiss(viewHolder.adapterPosition)
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                    val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
                    itemViewHolder.onItemSelected()
                }
                super.onSelectedChanged(viewHolder, actionState)
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                val itemViewHolder = viewHolder as ItemTouchHelperViewHolder
                itemViewHolder.onItemClear()
            }
        }

        override fun onItemMove(fromPosition: Int, toPosition: Int) {
            data.removeAt(fromPosition).apply {
                data.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
            }
            notifyItemMoved(fromPosition, toPosition)
        }

        override fun onItemDismiss(position: Int) {
            data.removeAt(position)
            notifyItemRemoved(position)
        }

        interface OnStartDragListener {
            fun onStartDrag(viewHolder: RecyclerView.ViewHolder)
        }

    }

}