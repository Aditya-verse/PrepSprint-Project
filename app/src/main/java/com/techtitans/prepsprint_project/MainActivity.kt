package com.techtitans.prepsprint_project

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.card.MaterialCardView
import com.techtitans.prepsprint_project.databinding.ActivityMainBinding
import com.techtitans.prepsprint_project.ui.userprofile.Userprofile

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // --- ADD THIS LINE TO REMOVE THE TOP BAR ---
        supportActionBar?.hide()
        // -------------------------------------------

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfile: MaterialCardView = findViewById(R.id.profile_icon)

        userProfile.setOnClickListener {
            findNavController(R.id.nav_host_fragment_activity_main)
                .navigate(R.id.userprofileFragment)
        }


        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as androidx.navigation.fragment.NavHostFragment

        val navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications,
                R.id.navigation_ai_chat
            )
        )

        binding.navView.setupWithNavController(navController)
    }
}