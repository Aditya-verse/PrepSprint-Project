package com.techtitans.prepsprint_project.ui.quiz

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
// 1. Ensure this matches your XML name exactly
import com.techtitans.prepsprint_project.databinding.FragmentAiQuizBinding

class QuizFragment : Fragment() {

    // 2. Changed from FragmentQuizBinding to FragmentAiQuizBinding
    private var _binding: FragmentAiQuizBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // 3. Changed inflation to match the correct class
        _binding = FragmentAiQuizBinding.inflate(inflater, container, false)

        binding.btnGenerateQuiz.setOnClickListener {
            val topic = binding.etTopic.text.toString().trim()

            if (topic.isEmpty()) {
                binding.etTopic.error = "Topic cannot be empty"
            } else {
                Toast.makeText(
                    requireContext(),
                    "Generating quiz for: $topic",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // 4. This is critical to prevent memory leaks in Fragments
        _binding = null
    }
}