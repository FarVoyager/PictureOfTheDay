package com.example.pictureoftheday.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.pictureoftheday.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.navigation.NavigationView

const val PREFERENCES = "PREF"

class BottomSheetDialogFragment : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navView = view.findViewById<NavigationView>(R.id.navigation_view)

        navView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_set_light -> {
                    if (isLightTheme) {
                        Toast.makeText(requireContext(), "Тема уже применена", Toast.LENGTH_SHORT).show()
                    } else {
                        isLightTheme = !isLightTheme
                        requireActivity().recreate()
                    }
                }
                R.id.navigation_set_dark -> {
                    if (!isLightTheme) {
                        Toast.makeText(requireContext(), "Тема уже применена", Toast.LENGTH_SHORT).show()
                    } else {
                        isLightTheme = !isLightTheme
                        requireActivity().recreate()
                    }
                }
            }
            true
        }
    }
}