package com.example.pictureoftheday.ui.main.viewPager

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.example.pictureoftheday.R
import com.example.pictureoftheday.databinding.FragmentPreviousDayFragmentOneBinding
import com.example.pictureoftheday.ui.main.viewmodel.AppState
import com.example.pictureoftheday.ui.main.viewmodel.PictureOfTheDayViewModel

private const val MINUS_DAYS_ONE = 1

class PreviousDayFragmentOne : Fragment() {

    private var _binding: FragmentPreviousDayFragmentOneBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentPreviousDayFragmentOneBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getData(MINUS_DAYS_ONE).observe(viewLifecycleOwner, { renderData(it) })
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
                        val webView = binding.yesterdayWebView
                        webView.layoutParams = binding.yesterdayImageView.layoutParams
                        webView.visibility = View.VISIBLE
                        binding.yesterdayImageView.visibility = View.GONE
                        webView.webViewClient = WebViewClient()
                        webView.settings.javaScriptEnabled = true
                        webView.settings.javaScriptCanOpenWindowsAutomatically = true
                        webView.settings.mediaPlaybackRequiresUserGesture = false
                        webView.webChromeClient = WebChromeClient();
                        webView.loadUrl(url)
                    } else {
                        //загрузка  картинки по url
                        binding.yesterdayImageView.visibility = View.VISIBLE
                        binding.yesterdayImageView.load(url) {
                            lifecycle(this@PreviousDayFragmentOne)
                            error(R.drawable.ic_load_error_vector)
                            placeholder(R.drawable.ic_no_photo_vector)
                        }
                    }
                    binding.yesterdayDate.text = viewModel.getPreviousDateForRequest(MINUS_DAYS_ONE)
                    binding.yesterdayHeader.text = serverResponseData.title
                    binding.yesterdayDescription.text = serverResponseData.explanation
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

}