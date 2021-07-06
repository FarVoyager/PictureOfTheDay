package com.example.pictureoftheday.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import coil.api.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.BottomSheetBinding
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.ui.main.viewmodel.AppState
import com.example.pictureoftheday.ui.main.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PictureOfTheDayFragment : Fragment() {

    private var _binding: FragmentPictureOfTheDayBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPictureOfTheDayBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData().observe(viewLifecycleOwner, { renderData(it) })

        setWikiFieldIconClickAction()
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setBottomAppBar(view)

    }

    private fun renderData(data: AppState) {
        when (data) {
            is AppState.Success -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE

                val serverResponseData = data.serverResponseData //является ли это нарушением MWWM?
                val url = serverResponseData.url
                if (url.isNullOrEmpty()) {
                    Toast.makeText(requireContext(), "Ошибка: пустой URL", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    //загрузка  картинки по url
                    binding.PODImageView.load(url) {
                        lifecycle(this@PictureOfTheDayFragment)
                        error(R.drawable.ic_load_error_vector)
                        placeholder(R.drawable.ic_no_photo_vector)
                    }

                    //заполнение bottom sheet данными из API
                    val bottomSheetTitleTextView: TextView? =
                        view?.findViewById(R.id.bottom_sheet_description_header)
                    bottomSheetTitleTextView?.text = serverResponseData.title

                    val bottomSheetDescriptionTextView: TextView? =
                        view?.findViewById(R.id.bottom_sheet_description)
                    bottomSheetDescriptionTextView?.text = serverResponseData.explanation
                }
            }
            is AppState.Loading -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.VISIBLE
            }
            is AppState.Error -> {
                binding.includedLoadingLayout.loadingLayout.visibility = View.GONE
                Toast.makeText(context, "ERROR", Toast.LENGTH_LONG).show()
                data.error.printStackTrace()
            }
        }
    }

    //поведение bottom sheet
    private fun setBottomSheetBehavior(bottomSheetLayout: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        //действия при изм. состояния bottom sheet
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_DRAGGING -> makeToast("DRAGGING")
                    BottomSheetBehavior.STATE_COLLAPSED -> makeToast("COLLAPSED")
                    BottomSheetBehavior.STATE_EXPANDED -> makeToast("EXPANDED")
                    BottomSheetBehavior.STATE_HALF_EXPANDED -> makeToast("HALF_EXPANDED")
                    BottomSheetBehavior.STATE_HIDDEN -> makeToast("HIDDEN")
                    BottomSheetBehavior.STATE_SETTLING -> makeToast("SETTLING")
                }
            }
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                makeToast("SLIDE!")
            }
        })
    }

    companion object {
        @JvmStatic
        fun newInstance() = PictureOfTheDayFragment()
        private var isMain = true
    }

    //установка bottom app bar
    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottom_app_bar)

        fab.setOnClickListener {
            if (isMain) {
                isMain = false
                bottomAppBar.navigationIcon = null
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_back_fab))
                bottomAppBar.replaceMenu(R.menu.menu_search)
            } else {
                isMain = true
                bottomAppBar.navigationIcon =
                    ContextCompat.getDrawable(context, R.drawable.ic_hamburger_menu_bottom_bar)
                bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                fab.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_plus_fab))
                bottomAppBar.replaceMenu(R.menu.bottom_menu)
            }
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.bottom_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.app_bar_fav ->
                requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container, StylesFragment())
                .addToBackStack(null)
                .commit()
            R.id.app_bar_settings -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, ChipsFragment())
                    .addToBackStack(null)
                    .commit()
            }
            android.R.id.home -> {
                activity?.let {
                    BottomSheetDialogFragment().show(it.supportFragmentManager, "tag")
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun makeToast(text: String) {
        Toast.makeText(requireContext(), text, Toast.LENGTH_SHORT).show()
    }

    private fun setWikiFieldIconClickAction() {
        binding.inputLayout.setEndIconOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            val wikiFieldText =
                Uri.parse("https://ru.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            intent.data = wikiFieldText
            startActivity(intent)
        }
    }

}