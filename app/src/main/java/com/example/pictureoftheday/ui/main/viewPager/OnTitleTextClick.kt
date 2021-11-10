package com.example.pictureoftheday.ui.main.viewPager

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat.startActivity
import java.net.URLEncoder

interface OnTitleTextClick {
    fun startWebSearchIntent(request: String) {
        val intent = Intent(Intent.ACTION_VIEW)
        val query = URLEncoder.encode(request, "UTF-8")
        val googleRequestString = Uri.parse("https://www.google.com/search?q=${query}")
        intent.data = googleRequestString
        startIntentActivity(intent)
    }

    fun startIntentActivity(intent: Intent)
}