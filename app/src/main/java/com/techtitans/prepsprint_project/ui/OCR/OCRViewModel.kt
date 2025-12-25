package com.techtitans.prepsprint_project.ui.OCR

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OCRViewModel : ViewModel() {

    private val _extractedText = MutableLiveData<String>()
    val extractedText: LiveData<String> = _extractedText

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun processImage(bitmap: Bitmap) {
        _isLoading.value = true
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Smart Formatting: Adds "AI" context to the raw text
                val formattedResult = formatAsAiInsight(visionText.text)
                _extractedText.value = formattedResult
                _isLoading.value = false
            }
            .addOnFailureListener {
                _extractedText.value = "Error: Could not extract text."
                _isLoading.value = false
            }
    }

    private fun formatAsAiInsight(rawText: String): String {
        if (rawText.isBlank()) return "No text found in image."

        return "✨ **AI INSIGHTS SUMMARY**\n" +
                "--------------------------------\n" +
                rawText +
                "\n--------------------------------\n" +
                "Processed by Smart OCR Pro"
    }
}