package com.techtitans.prepsprint_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class FlashScreen : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_flash_screen)
       /* ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }*/
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        // Find views
        val logo = findViewById<ImageView>(R.id.ic_prep_logo)
        val title = findViewById<TextView>(R.id.text_prepsprint)

        // 1. Cool Entry Animation (Fade in + Scale up)
        logo.animate().alpha(1f).scaleX(1.1f).scaleY(1.1f).setDuration(1200).withEndAction {
            // Gently scale back to normal
            logo.animate().scaleX(1f).scaleY(1f).setDuration(500).start()
        }.start()

        title.animate().alpha(1f).setDuration(1500).setStartDelay(400).start()

        // 2. Delay Logic
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginPage::class.java)
            startActivity(intent)

            // Add a smooth transition between activities
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

            finish()
        }, 3500)
    }
}