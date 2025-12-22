package com.techtitans.prepsprint_project.ui.OCR

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OCRViewModel : ViewModel() {

    private val _recognizedText = MutableLiveData<String>()
    val recognizedText: LiveData<String> = _recognizedText

    fun setText(text: String) {
        _recognizedText.value = text
    }
}
