package com.example.pictureoftheday.ui.main

import android.animation.Animator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import com.example.pictureoftheday.R

class SplashActivity : AppCompatActivity() {

    var handler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        findViewById<ImageView>(R.id.image_view_splash).animate()
            .scaleX(1.2f).scaleY(1.2f)
            .setInterpolator(AccelerateDecelerateInterpolator()).setDuration(400)
            .setListener(object: Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    scaleAnimation(1f, 1f)
                }
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })

        handler.postDelayed({
            startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            finish()
        }, 5000)
    }

    fun scaleAnimation(X: Float, Y:Float) {
        findViewById<ImageView>(R.id.image_view_splash).animate()
            .scaleX(X).scaleY(Y)
            .setInterpolator(AccelerateDecelerateInterpolator()).setDuration(400)
            .setListener(object: Animator.AnimatorListener {
                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                override fun onAnimationStart(animation: Animator?) {}
                override fun onAnimationCancel(animation: Animator?) {}
                override fun onAnimationRepeat(animation: Animator?) {}
            })
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }
}