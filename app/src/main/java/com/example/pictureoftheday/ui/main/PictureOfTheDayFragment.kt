package com.example.pictureoftheday.ui.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPictureOfTheDayBinding
import com.example.pictureoftheday.ui.main.animations.AnimationsFragmentBonus
import com.example.pictureoftheday.ui.main.recyclerview.RecyclerViewNotesFragment
import com.example.pictureoftheday.ui.main.utils.LayoutsFragment
import com.example.pictureoftheday.ui.main.viewPager.MainViewPagerFragment
import com.example.pictureoftheday.ui.main.viewmodel.AppState
import com.example.pictureoftheday.ui.main.viewmodel.PictureOfTheDayViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*

class PictureOfTheDayFragment : Fragment() {

    private var isWikiExpanded = false

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

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(0).observe(viewLifecycleOwner, { renderData(it) })

        binding.inputLayout.alpha = 0f //для анимации

        setWikiFieldIconClickAction()
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
        setBottomAppBar(view)
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
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
                    if (serverResponseData.mediaType == "video") {
                        showVideoContent(url)
                    } else {
                        showPhotoContent(url)
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

    private fun showPhotoContent(url: String) {
        binding.PODImageView.visibility = View.VISIBLE
        binding.PODImageView.load(url) {
            lifecycle(this@PictureOfTheDayFragment)
            error(R.drawable.ic_load_error_vector)
            placeholder(R.drawable.ic_no_photo_vector)
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private fun showVideoContent(url: String) {
        val webView = binding.yesterdayWebView
        webView.layoutParams = binding.PODImageView.layoutParams
        webView.visibility = View.VISIBLE
        binding.PODImageView.visibility = View.GONE
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.mediaPlaybackRequiresUserGesture = false
        webView.webChromeClient = WebChromeClient()
        webView.loadUrl(url)
    }

    //поведение bottom sheet
    private fun setBottomSheetBehavior(bottomSheetLayout: ConstraintLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    companion object {
        @JvmStatic
        fun newInstance() = PictureOfTheDayFragment()
    }

    //установка bottom app bar
    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)

        val fab = view.findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {

            ObjectAnimator.ofFloat(binding.starFabImageview, "rotation", 0f, 360f).start()
            ObjectAnimator.ofFloat(binding.starFabImageview, "translationY", -250f).start()
            binding.starFabImageview.animate()
                .alpha(0f)
                .setDuration(300)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .replace(R.id.container, MainViewPagerFragment())
                            .addToBackStack(null)
                            .commit()
                    }
                })


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
                    .replace(R.id.container, RecyclerViewNotesFragment())
                    .addToBackStack(null)
                    .commit()
            R.id.app_bar_settings -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, LayoutsFragment())
                    .addToBackStack(null)
                    .commit()
            }
            R.id.app_bar_view_pager -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, AnimationsFragmentBonus())
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

    private fun setWikiFieldIconClickAction() {
        binding.wikiButton.setOnClickListener {

            if (isWikiExpanded) {
                val intent = Intent(Intent.ACTION_VIEW)
                val wikiFieldText =
                    Uri.parse("https://ru.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
                intent.data = wikiFieldText
                startActivity(intent)
            } else {
                ObjectAnimator.ofFloat(binding.inputLayout, "translationX", -1050f).start()
                ObjectAnimator.ofFloat(binding.wikiButton, "elevation", 10f).start()
                binding.inputLayout.animate()
                    .alpha(1f).duration = 400
                isWikiExpanded = !isWikiExpanded
            }

        }
    }

}