package com.example.pictureoftheday.ui.main.recyclerview

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MotionEventCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.*
import kotlin.math.abs


class RecyclerViewNotesFragment : Fragment() {

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
            Pair(Data("Mars", "", false), false),
            Pair(Data("Earth", "Desc", true), false)


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
                    Toast.makeText(requireContext(), data.noteTitle, Toast.LENGTH_SHORT).show()
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
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Create a note")
                .setMessage("Which note do you want to add?")
                .setPositiveButton("Task") { dialog, which ->
                    adapter.appendItem(Pair(Data("Task", "Description"), true))

                }
                .setNegativeButton("Simple note") { dialog, which ->
                    adapter.appendItem(Pair(Data("Simple note", "Description"), false))

                }
            builder.create().apply { show() }


            //            adapter.appendItem()
        }
        itemTouchHelper = ItemTouchHelper(RecyclerViewAdapter.ItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(binding.lessonRecyclerView)

    }

    data class Data(
        val noteTitle: String = "Title",
        val noteDescription: String? = "Description",
        val isImaged: Boolean = false
    )

    class RecyclerViewAdapter(
        private var onListItemClickListener: OnListItemClickListener,
        private var data: MutableList<Pair<Data, Boolean>>,
        private val dragListener: OnStartDragListener
    ) : RecyclerView.Adapter<RecyclerViewAdapter.BaseViewHolder>(), ItemTouchHelperAdapter {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
            val itemBindingSimple =
                RecyclerItemSimpleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            val itemBindingImg =
                RecyclerItemImgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return when (viewType) {
                TYPE_SIMPLE -> SimpleNoteViewHolder(itemBindingSimple)
                else -> ImagedNoteViewHolder(itemBindingImg)
            }
        }

        override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
            when (getItemViewType(position)) {
                TYPE_SIMPLE -> {
                    holder as SimpleNoteViewHolder
                    holder.bind(data[position])
                }
                else -> {
                    holder as ImagedNoteViewHolder
                    holder.bind(data[position])
                }
            }
        }

        override fun getItemViewType(position: Int): Int {
            return when {
                data[position].first.isImaged -> TYPE_IMG
                else -> TYPE_SIMPLE
            }
        }

        override fun getItemCount(): Int {
            return data.size
        }

        fun appendItem(dataPair: Pair<Data, Boolean>) {
            data.add(dataPair)
            notifyItemInserted(itemCount - 1)
        }

        private fun generateItem(): Pair<Data, Boolean> {
            return Pair(Data("Mars", ""), false)
        }

        inner class SimpleNoteViewHolder(private val bindingSimple: RecyclerItemSimpleBinding) :
            BaseViewHolder(bindingSimple.root), ItemTouchHelperViewHolder {
            override fun bind(data: Pair<Data, Boolean>) {

                bindingSimple.expandDescriptionBtn.setOnClickListener {
                    onListItemClickListener.onItemClick(data.first)
                }
                bindingSimple.addItemImageView.setOnClickListener {
                    addItem(layoutPosition)
                    bindingSimple.removeItemImageView.setOnClickListener {
                        removeItem(layoutPosition)
                    }
                }
                bindingSimple.moveItemDown.setOnClickListener { moveItemDown(layoutPosition) }
                bindingSimple.moveItemUp.setOnClickListener { moveItemUp(layoutPosition) }
                bindingSimple.descriptionTextView.visibility =
                    if (data.second) View.VISIBLE else View.GONE
                bindingSimple.expandDescriptionBtn.setOnClickListener { toggleText(layoutPosition) }

                bindingSimple.dragHandleImageView.setOnTouchListener { v, event ->
                    if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                        dragListener.onStartDrag(this)
                    }
                    false
                }
            }

            override fun onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY)
            }

            override fun onItemClear() {
                itemView.setBackgroundColor(0)
            }
        }

        inner class ImagedNoteViewHolder(private val bindingImg: RecyclerItemImgBinding) :
            BaseViewHolder(bindingImg.root), ItemTouchHelperViewHolder {

            override fun bind(data: Pair<Data, Boolean>) {
                    bindingImg.checkBox.setOnClickListener {
                        onListItemClickListener.onItemClick(data.first)
                    }
                    bindingImg.addItemImageView.setOnClickListener {
                        addItem(layoutPosition)
                        bindingImg.removeItemImageView.setOnClickListener {
                            removeItem(layoutPosition)
                        }
                    }
                    bindingImg.moveItemDown.setOnClickListener { moveItemDown(layoutPosition) }
                    bindingImg.moveItemUp.setOnClickListener { moveItemUp(layoutPosition) }
                    bindingImg.descriptionTextView.visibility =
                        if (data.second) View.VISIBLE else View.GONE
                    bindingImg.expandDescriptionBtn.setOnClickListener { toggleText(layoutPosition) }

                    bindingImg.dragHandleImageView.setOnTouchListener { v, event ->
                        if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                            dragListener.onStartDrag(this)
                        }
                        false
                    }
                }
            override fun onItemSelected() {
                itemView.setBackgroundColor(Color.LTGRAY)
            }
            override fun onItemClear() {
                itemView.setBackgroundColor(0)
            }
        }

        private fun toggleText(layoutPosition: Int) {
            data[layoutPosition] = data[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        private fun moveItemUp(layoutPosition: Int) {
            layoutPosition.takeIf { it > 0 }?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition - 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition - 1)
            }
        }

        private fun removeItem(layoutPosition: Int) {
            data.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        private fun addItem(layoutPosition: Int) {
            data.add(layoutPosition, generateItem())
            notifyItemInserted(layoutPosition)
        }

        private fun moveItemDown(layoutPosition: Int) {
            layoutPosition.takeIf { it < data.size - 1}?.also { currentPosition ->
                data.removeAt(currentPosition).apply {
                    data.add(currentPosition + 1, this)
                }
                notifyItemMoved(currentPosition, currentPosition + 1)
            }
        }

        companion object {
            private const val TYPE_SIMPLE = 0
            private const val TYPE_IMG = 1
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

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
                    val width = viewHolder.itemView.width.toFloat()
                    val alpha = 1.0f - abs(dX) / width
                    viewHolder.itemView.alpha = alpha
                    viewHolder.itemView.translationX = dX
                } else {
                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                }
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

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.apply { inflate(R.menu.add_note_menu, menu) }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }

}