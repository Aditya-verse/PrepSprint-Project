package com.techtitans.prepsprint_project

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.techtitans.prepsprint_project.databinding.ActivityLoginPageBinding

class LoginPage : AppCompatActivity() {

    private lateinit var binding: ActivityLoginPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Hide ActionBar for the custom premium look
        supportActionBar?.hide()

        // 2. Enable modern Edge-to-Edge display
        enableEdgeToEdge()

        // 3. Initialize ViewBinding
        binding = ActivityLoginPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Main Login Button with basic validation
        binding.btnLogin.setOnClickListener {
            val email = binding.tilEmail.editText?.text.toString()
            val password = binding.tilPassword.editText?.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish() // Prevents user from going back to Login screen after logging in
            } else {
                showToast("Please enter email and password")
            }
        }

        // Social Login - Google (Uses the ID added in XML)
        binding.googleClickView.setOnClickListener {
            showToast("Google sign in clicked")
        }

        // Social Login - Facebook (Uses the ID added in XML)
        binding.facebookClickView.setOnClickListener {
            showToast("Facebook sign in clicked")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}