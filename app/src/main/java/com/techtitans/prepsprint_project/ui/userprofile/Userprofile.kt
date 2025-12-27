package com.techtitans.prepsprint_project.ui.userprofile

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
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

    // SharedPreferences
    private lateinit var sharedPreferences: SharedPreferences

    // Notification Manager
    private lateinit var notificationManager: NotificationManager

    // Permission launcher for notifications
    private val notificationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            savePreference(KEY_NOTIFICATIONS, true)
            notificationSwitch.isChecked = true
            Toast.makeText(requireContext(), "Notifications enabled", Toast.LENGTH_SHORT).show()
        } else {
            savePreference(KEY_NOTIFICATIONS, false)
            notificationSwitch.isChecked = false
            Toast.makeText(requireContext(), "Notification permission denied", Toast.LENGTH_SHORT)
                .show()
        }
    }

    companion object {
        private const val PREFS_NAME = "UserProfilePrefs"
        private const val KEY_NAME = "user_name"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_AVATAR = "user_avatar"
        private const val KEY_BIO = "user_bio"
        private const val KEY_PHONE = "user_phone"
        private const val KEY_DARK_THEME = "dark_theme"
        private const val KEY_NOTIFICATIONS = "notifications_enabled"
        private const val KEY_SOUND_EFFECTS = "sound_effects"
        private const val CHANNEL_ID = "PrepSprint_Notifications"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_userprofile, container, false)

        // Initialize SharedPreferences
        sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

        // Initialize Notification Manager
        notificationManager =
            requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Initialize views
        initializeViews(view)

        // Load saved data
        loadSavedData()

        // Setup listeners
        setupClickListeners()

        // Create notification channel
        createNotificationChannel()

        return view
    }

    private fun initializeViews(view: View) {
        // Profile section
        editButton = view.findViewById(R.id.ivEdit)
        avatarText = view.findViewById(R.id.tvAvatar)
        nameText = view.findViewById(R.id.tvName)
        emailText = view.findViewById(R.id.tvEmail)
        pointsText = view.findViewById(R.id.tvPoints)
        bioText = view.findViewById(R.id.tvBio)
        phoneText = view.findViewById(R.id.tvPhone)

        // Preference switches
        darkThemeSwitch = view.findViewById(R.id.switchDarkTheme)
        notificationSwitch = view.findViewById(R.id.switchNotifications)
        soundEffectsSwitch = view.findViewById(R.id.switchSoundEffects)

        // Buttons
        feedbackButton = view.findViewById(R.id.btnFeedback)
        logoutButton = view.findViewById(R.id.btnLogout)

        // Privacy & Security
        privacySecurityLayout = view.findViewById(R.id.layoutPrivacySecurity)
    }

    private fun loadSavedData() {
        // Load profile data
        nameText.text = sharedPreferences.getString(KEY_NAME, "XC") ?: "XC"
        emailText.text = sharedPreferences.getString(KEY_EMAIL, "dfg") ?: "dfg"
        avatarText.text = sharedPreferences.getString(KEY_AVATAR, "X") ?: "X"
        bioText.text =
            sharedPreferences.getString(KEY_BIO, "\"Ready to learn!\"") ?: "\"Ready to learn!\""
        phoneText.text = sharedPreferences.getString(KEY_PHONE, "Not set") ?: "Not set"

        // Load preferences (without triggering listeners)
        val isDarkTheme = sharedPreferences.getBoolean(KEY_DARK_THEME, true)
        val isNotificationsEnabled = sharedPreferences.getBoolean(KEY_NOTIFICATIONS, true)
        val isSoundEnabled = sharedPreferences.getBoolean(KEY_SOUND_EFFECTS, false)

        darkThemeSwitch.isChecked = isDarkTheme
        notificationSwitch.isChecked = isNotificationsEnabled
        soundEffectsSwitch.isChecked = isSoundEnabled
    }

    private fun saveProfileData(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun savePreference(key: String, value: Boolean) {
        sharedPreferences.edit().putBoolean(key, value).apply()
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
            savePreference(KEY_DARK_THEME, isChecked)
            applyTheme(isChecked)
            Toast.makeText(
                requireContext(),
                if (isChecked) "Dark theme enabled" else "Light theme enabled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Notification switch
        notificationSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Check if we need to request permission (Android 13+)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    when {
                        ContextCompat.checkSelfPermission(
                            requireContext(),
                            Manifest.permission.POST_NOTIFICATIONS
                        ) == PackageManager.PERMISSION_GRANTED -> {
                            // Permission already granted
                            savePreference(KEY_NOTIFICATIONS, true)
                            Toast.makeText(
                                requireContext(),
                                "Notifications enabled",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        else -> {
                            // Request permission
                            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }
                } else {
                    // Android 12 and below - no runtime permission needed
                    savePreference(KEY_NOTIFICATIONS, true)
                    Toast.makeText(requireContext(), "Notifications enabled", Toast.LENGTH_SHORT)
                        .show()
                }
            } else {
                savePreference(KEY_NOTIFICATIONS, false)
                Toast.makeText(requireContext(), "Notifications disabled", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        // Sound effects switch
        soundEffectsSwitch.setOnCheckedChangeListener { _, isChecked ->
            savePreference(KEY_SOUND_EFFECTS, isChecked)
            Toast.makeText(
                requireContext(),
                if (isChecked) "Sound effects enabled" else "Sound effects disabled",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Privacy & Security navigation
        privacySecurityLayout.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Opening Privacy & Security settings",
                Toast.LENGTH_SHORT
            ).show()
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

    private fun applyTheme(isDarkTheme: Boolean) {
        if (isDarkTheme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun handleNotificationPermission(isEnabled: Boolean) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (channel != null) {
                // Channel exists, you can guide user to settings if they want to change
                if (!isEnabled) {
                    // Optionally inform user they need to disable in system settings
                    Toast.makeText(
                        requireContext(),
                        "To fully disable, go to App Settings > Notifications",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "PrepSprint Notifications"
            val descriptionText = "Notifications for quiz reminders and updates"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun showEditProfileDialog() {
        val dialogView =
            LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_profile, null)

        val nameInput =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etName)
        val emailInput =
            dialogView.findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etEmail)

        // Set current values
        nameInput.setText(nameText.text.toString())
        emailInput.setText(emailText.text.toString())

        MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Save") { dialog, _ ->
                val newName = nameInput.text.toString().trim()
                val newEmail = emailInput.text.toString().trim()

                if (newName.isNotEmpty() && newEmail.isNotEmpty()) {
                    // Update UI
                    nameText.text = newName
                    emailText.text = newEmail

                    // Update avatar with first letter
                    val newAvatar = newName.firstOrNull()?.toString()?.uppercase() ?: "X"
                    avatarText.text = newAvatar

                    // Save to SharedPreferences
                    saveProfileData(KEY_NAME, newName)
                    saveProfileData(KEY_EMAIL, newEmail)
                    saveProfileData(KEY_AVATAR, newAvatar)

                    Toast.makeText(
                        requireContext(),
                        "Profile updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT)
                        .show()
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
                val selectedLetter = letters[which]
                avatarText.text = selectedLetter

                // Save to SharedPreferences
                saveProfileData(KEY_AVATAR, selectedLetter)

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
            setTextColor(resources.getColor(android.R.color.white, null))
            setHintTextColor(resources.getColor(android.R.color.darker_gray, null))
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Bio")
            .setView(bioInput)
            .setPositiveButton("Save") { dialog, _ ->
                val newBio = bioInput.text.toString().trim()
                if (newBio.isNotEmpty()) {
                    val formattedBio = "\"$newBio\""
                    bioText.text = formattedBio

                    // Save to SharedPreferences
                    saveProfileData(KEY_BIO, formattedBio)

                    Toast.makeText(requireContext(), "Bio updated successfully", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(requireContext(), "Bio cannot be empty", Toast.LENGTH_SHORT)
                        .show()
                }
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
            setTextColor(resources.getColor(android.R.color.white, null))
            setHintTextColor(resources.getColor(android.R.color.darker_gray, null))
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Edit Phone")
            .setView(phoneInput)
            .setPositiveButton("Save") { dialog, _ ->
                val phone = phoneInput.text.toString().trim()
                val phoneToSave = if (phone.isNotEmpty()) phone else "Not set"
                phoneText.text = phoneToSave

                // Save to SharedPreferences
                saveProfileData(KEY_PHONE, phoneToSave)

                Toast.makeText(requireContext(), "Phone updated successfully", Toast.LENGTH_SHORT)
                    .show()
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
            setTextColor(resources.getColor(android.R.color.white, null))
            setHintTextColor(resources.getColor(android.R.color.darker_gray, null))
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Send Feedback")
            .setView(feedbackInput)
            .setPositiveButton("Send") { dialog, _ ->
                val feedback = feedbackInput.text.toString().trim()
                if (feedback.isNotEmpty()) {
                    // TODO: Send feedback to server
                    Toast.makeText(
                        requireContext(),
                        "Thank you for your feedback!",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(requireContext(), "Please enter feedback", Toast.LENGTH_SHORT)
                        .show()
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
                // Clear user session data if needed
                // sharedPreferences.edit().clear().apply()

                Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT)
                    .show()
                dialog.dismiss()

                // TODO: Navigate to login screen
                // Example navigation:
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

    override fun onPause() {
        super.onPause()
        // Data is automatically saved when changed, no need to save here again
    }
}