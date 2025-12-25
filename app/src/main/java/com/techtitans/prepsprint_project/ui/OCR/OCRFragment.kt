package com.techtitans.prepsprint_project.ui.OCR

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.techtitans.prepsprint_project.databinding.FragmentOcrBinding
import java.io.File
import java.io.FileOutputStream

class OCRFragment : Fragment() {

    private var _binding: FragmentOcrBinding? = null
    private val binding get() = _binding!!
    private val viewModel: OCRViewModel by viewModels()
    private var currentBitmap: Bitmap? = null

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        bitmap?.let {
            binding.ivPreview.setImageBitmap(it)
            currentBitmap = it
        }
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver, it)
            binding.ivPreview.setImageBitmap(bitmap)
            currentBitmap = bitmap
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentOcrBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnCamera.setOnClickListener { cameraLauncher.launch(null) }
        binding.btnGallery.setOnClickListener { galleryLauncher.launch("image/*") }

        binding.btnExtract.setOnClickListener {
            currentBitmap?.let { viewModel.processImage(it) }
        }

        // FIX: Ensure text exists before running PDF logic
        binding.btnSavePdf.setOnClickListener {
            val text = viewModel.extractedText.value
            if (!text.isNullOrBlank()) {
                val file = generatePdfFile(text)
                if (file != null) Toast.makeText(requireContext(), "Saved to Documents", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnShare.setOnClickListener {
            val text = viewModel.extractedText.value
            if (!text.isNullOrBlank()) {
                val file = generatePdfFile(text)
                file?.let { sharePdfFile(it) }
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { loading ->
            binding.loader.visibility = if (loading) View.VISIBLE else View.GONE
        }
        viewModel.extractedText.observe(viewLifecycleOwner) { text ->
            binding.insightCard.visibility = View.VISIBLE
            binding.tvResult.text = text
        }
    }

    private fun generatePdfFile(content: String): File? {
        val pdfDocument = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
        val page = pdfDocument.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { textSize = 12f }

        var yPos = 50f
        content.split("\n").forEach { line ->
            if (yPos < 800f) {
                canvas.drawText(line, 50f, yPos, paint)
                yPos += 20f
            }
        }
        pdfDocument.finishPage(page)

        // FIX: Using internal cache or files dir to avoid permission issues
        val file = File(requireContext().cacheDir, "OCR_Report.pdf")

        return try {
            val outputStream = FileOutputStream(file)
            pdfDocument.writeTo(outputStream)
            outputStream.close()
            pdfDocument.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            pdfDocument.close()
            null
        }
    }

    private fun sharePdfFile(file: File) {
        try {
            val authority = "${requireContext().packageName}.fileprovider"
            val uri: Uri = FileProvider.getUriForFile(requireContext(), authority, file)

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "application/pdf"
                putExtra(Intent.EXTRA_STREAM, uri)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            startActivity(Intent.createChooser(intent, "Share Report"))
        } catch (e: Exception) {
            Toast.makeText(requireContext(), "Share failed: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}