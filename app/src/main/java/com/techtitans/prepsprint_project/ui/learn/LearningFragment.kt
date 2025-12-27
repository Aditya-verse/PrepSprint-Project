package com.techtitans.prepsprint_project.ui.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.techtitans.prepsprint_project.databinding.FragmentAudioLearningBinding

/**
 * LearningFragment handles the logic for the Audio Learning feature.
 */
class LearningFragment : Fragment() {

    // View Binding variable: initialized as null to handle fragment lifecycle safely
    private var _binding: FragmentAudioLearningBinding? = null

    // This property is only valid between onCreateView and onDestroyView
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout using the binding class generated from fragment_audio_learning.xml
        _binding = FragmentAudioLearningBinding.inflate(inflater, container, false)

        setupClickListeners()

        return binding.root
    }

    private fun setupClickListeners() {
        // Accessing the button and input field via the binding object
        binding.generateButton.setOnClickListener {
            val topic = binding.inputField.text.toString().trim()

            if (topic.isNotEmpty()) {
                // Feedback for the user
                Toast.makeText(
                    requireContext(),
                    "Generating audio lesson for: $topic",
                    Toast.LENGTH_SHORT
                ).show()

                // TODO: Implement AI generation or navigation to a player screen
            } else {
                // Display error directly on the EditText
                binding.inputField.error = "Please enter a topic"
                Toast.makeText(requireContext(), "Topic cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Crucial: Set to null to prevent memory leaks as fragments outlive their views
        _binding = null
    }
}