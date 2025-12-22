package com.techtitans.prepsprint_project.ui.OCR



import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.techtitans.prepsprint_project.databinding.FragmentOcrBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class OCRFragment : Fragment() {

    private var _binding: FragmentOcrBinding? = null
    private val binding get() = _binding!!

    private val viewModel: OCRViewModel by viewModels()

    private val IMAGE_REQUEST = 100

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentOcrBinding.inflate(inflater, container, false)

        binding.btnCapture.setOnClickListener {
            openCamera()
        }

        viewModel.recognizedText.observe(viewLifecycleOwner) {
            binding.tvResult.text = it
        }

        return binding.root
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            val bitmap = data?.extras?.get("data") as Bitmap
            processImage(bitmap)
        }
    }

    private fun processImage(bitmap: Bitmap) {

        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                viewModel.setText(visionText.text)
            }
            .addOnFailureListener {
                viewModel.setText("Failed to recognize text")
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
