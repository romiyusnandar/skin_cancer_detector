package com.dicoding.asclepius.view

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityResultBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.local.LocalResponse
import com.dicoding.asclepius.utils.ViewModelFactory
import com.dicoding.asclepius.viewmodel.ResultViewModel
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding
    private lateinit var imageClassifierHelper: ImageClassifierHelper
    private val viewModel: ResultViewModel by viewModels { ViewModelFactory.getInstance(this) }
    private var result: LocalResponse? = null

    companion object {
        const val EXTRA_IMAGE_URI = "extra_image_uri"
        const val EXTRA_RESULT = "extra_result"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageUri = intent.getStringExtra(EXTRA_IMAGE_URI)?.let { Uri.parse(it) }
        val resultData = getParcelableExtraCompat<LocalResponse>(EXTRA_RESULT)

        when {
            imageUri != null -> handleImageClassification(imageUri)
            resultData != null -> handleExistingResult(resultData)
        }

        binding.btnBack.setOnClickListener { goBack() }
    }

    private fun handleImageClassification(imageUri: Uri) {
        binding.resultImage.setImageURI(imageUri)
        setupImageClassifier(imageUri)
        imageClassifierHelper.classifyStaticImage(imageUri)
        binding.btnSave.setOnClickListener {
            result?.let(viewModel::insert)
            goBack()
        }
    }

    private fun handleExistingResult(resultData: LocalResponse) {
        result = resultData
        updateView(Uri.parse(resultData.imageUri), resultData.label, resultData.score)
        binding.btnSave.apply {
            text = getString(R.string.delete)
            setOnClickListener {
                viewModel.delete(resultData)
                finish()
            }
        }
    }

    private fun setupImageClassifier(imageUri: Uri) {
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    Toast.makeText(applicationContext, error, Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: List<Classifications>?, inferenceTime: Long) {
                    runOnUiThread {
                        results?.firstOrNull()?.categories?.maxByOrNull { it.score }?.let { category ->
                            updateView(imageUri, category.label, category.score)
                            result = LocalResponse(
                                timestamp = System.currentTimeMillis(),
                                imageUri = imageUri.toString(),
                                label = category.label,
                                score = category.score
                            )
                        } ?: run {
                            binding.resultText.text = getString(R.string.no_results_found)
                        }
                    }
                }
            }
        )
    }

    @SuppressLint("SetTextI18n")
    private fun updateView(imageUri: Uri, label: String, score: Float) {
        binding.resultImage.setImageURI(imageUri)
        binding.resultText.text = "${NumberFormat.getPercentInstance().format(score)} $label"
        binding.linearProgressBar.progress = if (label == "Cancer") {
            (score * 100).toInt()
        } else {
            binding.resultText.setTextColor(getColor(R.color.secondary))
            100 - (score * 100).toInt()
        }
    }

    private fun goBack() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    private inline fun <reified T> getParcelableExtraCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(key)
        }
    }
}
