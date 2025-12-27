package com.techtitans.prepsprint_project.ui.userprofile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.switchmaterial.SwitchMaterial
import com.techtitans.prepsprint_project.R

class Userprofile : Fragment() {

    // UI Components
    private lateinit var editButton: ImageView
    private lateinit var avatarText: TextView
    private lateinit var nameText: TextView
    private lateinit var emailText: TextView
    private lateinit var pointsText: TextView
    private lateinit var bioText: TextView
    private lateinit var phoneText: TextView

    // Preference Switches
    private lateinit var darkThemeSwitch: SwitchMaterial
    private lateinit var notificationSwitch: SwitchMaterial
    private lateinit var soundEffectsSwitch: SwitchMaterial

    // Buttons
    private lateinit var feedbackButton: MaterialButton
    private lateinit var logoutButton: MaterialButton

    // Privacy & Security Layout
    private lateinit var privacySecurityLayout: View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_userprofile, container, false)

        // Initialize views
        initializeViews(view)

        // Setup listeners
        setupClickListeners()

        return view
    }

    private fun initializeViews(view: View) {
        // Profile section
        editButton = view.findViewById(R.id.ivEdit) // You'll need to add android:id="@+id/ivEdit" to the edit ImageView
        avatarText = view.findViewById(R.id.tvAvatar) // Add android:id="@+id/tvAvatar" to avatar TextView
        nameText = view.findViewById(R.id.tvName) // Add android:id="@+id/tvName" to name TextView
        emailText = view.findViewById(R.id.tvEmail) // Add android:id="@+id/tvEmail" to email TextView
        pointsText = view.findViewById(R.id.tvPoints) // Add android:id="@+id/tvPoints" to points TextView
        bioText = view.findViewById(R.id.tvBio) // Add android:id="@+id/tvBio" to bio TextView
        phoneText = view.findViewById(R.id.tvPhone) // Add android:id="@+id/tvPhone" to phone TextView

        // Preference switches
        darkThemeSwitch = view.findViewById(R.id.switchDarkTheme) // Add android:id="@+id/switchDarkTheme"
        notificationSwitch = view.findViewById(R.id.switchNotifications) // Add android:id="@+id/switchNotifications"
        soundEffectsSwitch = view.findViewById(R.id.switchSoundEffects) // Add android:id="@+id/switchSoundEffects"

        // Buttons
        feedbackButton = view.findViewById(R.id.btnFeedback)
        logoutButton = view.findViewById(R.id.btnLogout)

        // Privacy & Security
        privacySecurityLayout = view.findViewById(R.id.layoutPrivacySecurity) // Add android:id="@+id/layoutPrivacySecurity" to privacy LinearLayout
    }

    private fun setupClickListeners() {
        // Edit profile button
        editButton.setOnClickListener {
            showEditProfileDialog()
        }

        // Avatar click to change
        avatarText.setOnClickListener {
            showAvatarChangeDialog()
        }

        // Bio click to edit
        bioText.setOnClickListener {
            showEditBioDialog()
        }

        // Phone click to edit
        phoneText.setOnClickListener {
            showEditPhoneDialog()
        }

        // Dark theme switch
        darkThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Dark theme enabled", Toast.LENGTH_SHORT).show()
                // TODO: Implement theme change
            } else {
                Toast.makeText(requireContext(), "Light theme enabled", Toast.LENGTH_SHORT).show()
                // TODO: Implement theme change
            }
        }

        // Notification switch
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Notifications enabled", Toast.LENGTH_SHORT).show()
                // TODO: Enable notifications
            } else {
                Toast.makeText(requireContext(), "Notifications disabled", Toast.LENGTH_SHORT).show()
                // TODO: Disable notifications
            }
        }

        // Sound effects switch
        soundEffectsSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Toast.makeText(requireContext(), "Sound effects enabled", Toast.LENGTH_SHORT).show()
                // TODO: Enable sound effects
            } else {
                Toast.makeText(requireContext(), "Sound effects disabled", Toast.LENGTH_SHORT).show()
                // TODO: Disable sound effects
            }
        }

        // Privacy & Security navigation
        privacySecurityLayout.setOnClickListener {
            Toast.makeText(requireContext(), "Opening Privacy & Security settings", Toast.LENGTH_SHORT).show()
            // TODO: Navigate to privacy settings screen
        }

        // Feedback button
        feedbackButton.setOnClickListener {
            showFeedbackDialog()
        }

        // Logout button
        logoutButton.setOnClickListener {
            showLogoutDialog()
        }
    }

    private fun showEditProfileDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)

        val nameInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etName)
        val emailInput = dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEmail)

        // Set current values
        nameInput.setText(nameText.text.toString())
        emailInput.setText(emailText.text.toString())

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = nameInput.text.toString()
                val newEmail = emailInput.text.toString()

                if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                    nameText.text = newName
                    emailText.text = newEmail
                    // Update avatar with first letter
                    avatarText.text = newName.firstOrNull()?.toString()?.uppercase() ?: "X"
                    Toast.makeText(requireContext(), "Profile updated", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showAvatarChangeDialog() {
        val letters = ('A'..'Z').map { it.toString() }.toTypedArray()

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Choose Avatar Letter")
            .setItems(letters) { dialog, which ->
                avatarText.text = letters[which]
                Toast.makeText(requireContext(), "Avatar updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditBioDialog() {
        val bioInput = EditText(requireContext()).apply {
            hint = "Enter your bio"
            setText(bioText.text.toString().replace("\"", ""))
            setPadding(60, 40, 60, 40)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Bio")
            .setView(bioInput)
            .setPositiveButton("Save") { dialog, _ ->
                bioText.text = "\"${bioInput.text}\""
                Toast.makeText(requireContext(), "Bio updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showEditPhoneDialog() {
        val phoneInput = EditText(requireContext()).apply {
            hint = "Enter phone number"
            inputType = android.text.InputType.TYPE_CLASS_PHONE
            if (phoneText.text.toString() != "Not set") {
                setText(phoneText.text.toString())
            }
            setPadding(60, 40, 60, 40)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Phone")
            .setView(phoneInput)
            .setPositiveButton("Save") { dialog, _ ->
                val phone = phoneInput.text.toString()
                phoneText.text = if (phone.isNotEmpty()) phone else "Not set"
                Toast.makeText(requireContext(), "Phone updated", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showFeedbackDialog() {
        val feedbackInput = EditText(requireContext()).apply {
            hint = "Enter your feedback"
            minLines = 4
            setPadding(60, 40, 60, 40)
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Send Feedback")
            .setView(feedbackInput)
            .setPositiveButton("Send") { dialog, _ ->
                val feedback = feedbackInput.text.toString()
                if (feedback.isNotEmpty()) {
                    // TODO: Send feedback to server
                    Toast.makeText(requireContext(), "Thank you for your feedback!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "Please enter feedback", Toast.LENGTH_SHORT).show()
                }
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Logout")
            .setMessage("Are you sure you want to logout?")
            .setPositiveButton("Logout") { dialog, _ ->
                // TODO: Clear user session/data
                // TODO: Navigate to login screen
                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                dialog.dismiss()

                // Example: Navigate back or to login activity
                // findNavController().navigate(R.id.action_userprofile_to_login)
                // or
                // startActivity(Intent(requireContext(), LoginActivity::class.java))
                // requireActivity().finish()
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}