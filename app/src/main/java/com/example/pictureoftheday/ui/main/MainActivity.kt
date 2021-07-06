package com.example.pictureoftheday.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pictureoftheday.R

var isLightTheme = true //для изменения темы

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
    }

    private fun setTheme() {
        when (isLightTheme) {
            true -> {
                setTheme(R.style.AppTheme)
            }
            false -> {
                setTheme(R.style.AppThemeDark)
            }
        }
    }
}