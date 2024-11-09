package com.dicoding.asclepius.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.FragmentHomeBinding
import com.dicoding.asclepius.view.ResultActivity
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val TAG = "HomeFragment"
    }

    private var currentImageUri: Uri? = null
    private var croppedImageUri: Uri? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.galleryButton.setOnClickListener { startGallery() }

        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
                moveToResult()
            } ?: run {
                showToast(getString(R.string.please_select_image))
            }
        }
    }

    private fun startGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, getString(R.string.choose_image))
        launchGallery.launch(chooser)
    }

    private val launchGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg = result.data?.data
            selectedImg?.let { uri ->
                currentImageUri = uri
                showImage()
                resizeUCrop(uri)
            } ?: showToast(getString(R.string.error_select_image))
        }
    }

    private fun resizeUCrop(sourceUri: Uri) {
        val fileName = "cropped_img_${System.currentTimeMillis()}.jpg"
        val destinationUri = Uri.fromFile(File(requireContext().cacheDir, fileName))
        UCrop.of(sourceUri, destinationUri)
            .withAspectRatio(1f, 1f)
            .withMaxResultSize(1000, 1000)
            .start(requireContext(), this)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == UCrop.REQUEST_CROP) {
            when (resultCode) {
                AppCompatActivity.RESULT_OK -> {
                    val resultUri = UCrop.getOutput(data!!)
                    resultUri?.let {
                        showCroppedImage(resultUri)
                    } ?: showToast(getString(R.string.error_crop_image))
                }
                UCrop.RESULT_ERROR -> {
                    val cropError = UCrop.getError(data!!)
                    showToast("Crop error: ${cropError?.message}")
                    resetImageSelection()
                }
                AppCompatActivity.RESULT_CANCELED -> {
                    resetImageSelection()
                }
            }
        }
    }

    private fun resetImageSelection() {
        currentImageUri = null
        croppedImageUri = null
        binding.previewImageView.setImageResource(R.drawable.ic_place_holder)
    }

    private fun showImage() {
        currentImageUri?.let {
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        val intent = Intent(requireContext(), ResultActivity::class.java)
        croppedImageUri?.let { uri ->
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun moveToResult() {
        Log.d(TAG, "Moving to ResultActivity")
        val intent = Intent(requireContext(), ResultActivity::class.java)
        croppedImageUri?.let { uri ->
            intent.putExtra(ResultActivity.EXTRA_IMAGE_URI, uri.toString())
            startActivity(intent)
        } ?: showToast(getString(R.string.image_classifier_failed))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showCroppedImage(uri: Uri) {
        binding.previewImageView.setImageURI(uri)
        croppedImageUri = uri
        currentImageUri = uri
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}