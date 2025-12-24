package com.techtitans.prepsprint_project.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.techtitans.prepsprint_project.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupClickListeners()
        // Here you can set dynamic data from your database later
        setDashboardData("Aditya", 1, 12)
    }

    private fun setupClickListeners() {
        // 1. Streak Card Click
        binding.streakCard.setOnClickListener {
            showToast("Keep it up! 1-day streak is just the beginning.")
            animateClick(it)
        }

        // 2. Exam Countdown Card Click
        binding.countdownCard.setOnClickListener {
            showToast("Final exams start in 12 days. Let's study!")
            animateClick(it)
        }

        // 3. Daily Micro-Challenge Card Click
        binding.challengeCard.setOnClickListener {
            showToast("Launching 5-min Logic Quiz...")
            animateClick(it)
        }

        // 4. Scan Notes Card Click
        binding.scanNotesCard.setOnClickListener {
            showToast("Opening AI Camera Scanner...")
            animateClick(it)
        }

        // 5. Progress Card (Graph) Click
        binding.progressCard.setOnClickListener {
            showToast("Opening detailed analytics...")
        }
    }

    private fun setDashboardData(name: String, streak: Int, daysLeft: Int) {
        binding.textHello.text = "Hello, $name 👋"
        binding.tvDaysLeft.text = "$daysLeft Days"
        // You can use binding.streakCard to find child views if needed
    }

    // A simple function to show messages
    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    // Simple visual feedback for clicks
    private fun animateClick(view: View) {
        view.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
            view.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}