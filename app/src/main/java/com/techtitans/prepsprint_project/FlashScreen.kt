package com.techtitans.prepsprint_project

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FlashScreen : AppCompatActivity() {

    private lateinit var logo: ImageView
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Enable Edge-to-Edge first
        enableEdgeToEdge()
        setContentView(R.layout.activity_flash_screen)

        // 2. Hide ActionBar
        supportActionBar?.hide()

        // 3. Fix: Ensure the ID matches your XML root (android:id="@+id/main")
        val mainView = findViewById<android.view.View>(R.id.main)
        ViewCompat.setOnApplyWindowInsetsListener(mainView) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        logo = findViewById(R.id.ic_prep_logo)
        title = findViewById(R.id.text_prepsprint)

        startSplashAnimation()

        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }, 3800)
    }

    private fun startSplashAnimation() {
        logo.alpha = 0f
        logo.scaleX = 0.6f
        logo.scaleY = 0.6f

        title.alpha = 0f
        title.translationY = 50f

        val logoScaleX = ObjectAnimator.ofFloat(logo, "scaleX", 0.6f, 1f)
        val logoScaleY = ObjectAnimator.ofFloat(logo, "scaleY", 0.6f, 1f)
        val logoAlpha = ObjectAnimator.ofFloat(logo, "alpha", 0f, 1f)

        val logoAnimatorSet = AnimatorSet().apply {
            playTogether(logoScaleX, logoScaleY, logoAlpha)
            duration = 1000
            interpolator = OvershootInterpolator(1.2f)
        }

        val titleAlpha = ObjectAnimator.ofFloat(title, "alpha", 0f, 1f)
        val titleTranslate = ObjectAnimator.ofFloat(title, "translationY", 50f, 0f)

        val titleAnimatorSet = AnimatorSet().apply {
            playTogether(titleAlpha, titleTranslate)
            duration = 800
            interpolator = AccelerateDecelerateInterpolator()
        }

        val fullSet = AnimatorSet()
        fullSet.playSequentially(logoAnimatorSet, titleAnimatorSet)
        fullSet.doOnEnd {
            startLogoBreathingPulse()
        }
        fullSet.start()
    }

    private fun startLogoBreathingPulse() {
        val pulse = ValueAnimator.ofFloat(1f, 1.05f, 1f).apply {
            duration = 2000
            repeatCount = ValueAnimator.INFINITE
            interpolator = AccelerateDecelerateInterpolator()
        }
        pulse.addUpdateListener { animator ->
            val scale = animator.animatedValue as Float
            logo.scaleX = scale
            logo.scaleY = scale
        }
        pulse.start()
    }
}