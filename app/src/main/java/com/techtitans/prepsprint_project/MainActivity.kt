package com.techtitans.prepsprint_project

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.techtitans.prepsprint_project.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#0D111D")
        supportActionBar?.hide()

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        navController = navHostFragment.navController

        // Handle Manual Navigation for custom animations
        binding.navView.setOnItemSelectedListener { item ->
            if (item.itemId != navController.currentDestination?.id) {
                navigateWithSlide(item.itemId)
            }
            true
        }

        // UPDATED: Click listener on the Container (CardView) instead of just the icon
        // This ensures the ripple effect is contained within the circle nicely
        binding.profileContainer.setOnClickListener {
            navController.navigate(R.id.userprofileFragment)
        }

        // Keep the bar moving whenever destination changes & Handle Profile Blue Circle
        // ... inside onCreate ...

        navController.addOnDestinationChangedListener { _, destination, _ ->

            // 1. Existing Indicator Animation
            animateIndicator(destination.id)

            // 2. NEW: Subtle "Ring" indication for Profile
            if (destination.id == R.id.userprofileFragment) {
                // Active: Transparent BG, Blue Icon, Blue Ring (Stroke)
                binding.profileContainer.strokeWidth = 4 // Thickness of the ring (in pixels)
                binding.profileContainer.setCardBackgroundColor(Color.TRANSPARENT)
                binding.profileIcon.setColorFilter(Color.parseColor("#6366F1")) // Icon turns blue too
            } else {
                // Inactive: No Ring, Grey Icon
                binding.profileContainer.strokeWidth = 0
                binding.profileContainer.setCardBackgroundColor(Color.TRANSPARENT)
                binding.profileIcon.setColorFilter(Color.parseColor("#8E94A4"))
            }
        }
    }

    private fun navigateWithSlide(toId: Int) {
        val fromId = navController.currentDestination?.id ?: return

        val navOrder = listOf(
            R.id.navigation_home,
            R.id.navigation_quiz,
            R.id.navigation_scan,
            R.id.navigation_learn,
            R.id.navigation_ai_chat
        )

        val fromIndex = navOrder.indexOf(fromId)
        val toIndex = navOrder.indexOf(toId)

        val navOptions = if (toIndex > fromIndex) {
            // Sliding Right
            NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_right)
                .setExitAnim(R.anim.slide_out_left)
                .build()
        } else {
            // Sliding Left
            NavOptions.Builder()
                .setEnterAnim(R.anim.slide_in_left)
                .setExitAnim(R.anim.slide_out_right)
                .build()
        }

        navController.navigate(toId, null, navOptions)
    }

    private fun animateIndicator(destinationId: Int) {
        val navView = binding.navView
        val indicator = binding.navIndicator

        navView.post {
            val itemView = navView.findViewById<View>(destinationId) ?: return@post

            // Calculate center
            val targetX = itemView.x + (itemView.width / 2f) - (indicator.width / 2f)

            // FLUID SLIDE: Move + Stretch (Matches your video)
            indicator.animate()
                .translationX(targetX)
                .scaleX(1.4f) // Stretch while moving
                .setDuration(400)
                .setInterpolator(AnticipateOvershootInterpolator(1.2f))
                .withEndAction {
                    // Snap back to normal size
                    indicator.animate().scaleX(1f).setDuration(200).start()
                }
                .start()
        }
    }
}