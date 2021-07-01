package com.example.pictureoftheday.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pictureoftheday.R
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup

class ChipsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chips, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val chipGroup = view.findViewById<ChipGroup>(R.id.chip_group)
        chipGroup.setOnCheckedChangeListener { _, checkedId ->
            chipGroup.findViewById<Chip>(checkedId)?.let {
                Toast.makeText(context, "Выбран ${it.text}", Toast.LENGTH_SHORT).show()
            }
        }

        val chip = view.findViewById<Chip>(R.id.chip_close)
        chip.setOnCloseIconClickListener {
            Toast.makeText(context, "Close icon is clicked", Toast.LENGTH_SHORT).show()
        }
    }

    companion object {
        fun newInstance() = ChipsFragment()
    }
}