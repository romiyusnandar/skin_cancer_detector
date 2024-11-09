package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.core.vision.ImageProcessingOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier

class ImageClassifierHelper(
    private val context: Context,
    private val classifierListener: ClassifierListener?,
    private val threshold: Float = 0.1f,
    private val maxResults: Int = 2,
    private val modelName: String = "cancer_classification.tflite"
) {
    private var imageClassifier: ImageClassifier? = null

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val optionsBuilder = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseOptionsBuilder = BaseOptions.builder().setNumThreads(2)

        optionsBuilder.setBaseOptions(baseOptionsBuilder.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                modelName,
                optionsBuilder.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError("Image classifier failed to initialize.")
            Log.e(TAG, "TFLite failed to load model: ${e.message}")
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val imageProcessor = ImageProcessor.Builder().build()

        val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)

        bitmap?.let {
            val tensorImage = imageProcessor.process(TensorImage.fromBitmap(it))

            val imageProcessingOptions = ImageProcessingOptions.builder().build()

            val inferenceTimeStart = SystemClock.uptimeMillis()
            val results = imageClassifier?.classify(tensorImage, imageProcessingOptions)
            val inferenceTime = SystemClock.uptimeMillis() - inferenceTimeStart
            classifierListener?.onResults(results, inferenceTime)
        }
    }

    interface ClassifierListener {
        fun onError(error: String)
        fun onResults(results: List<Classifications>?, inferenceTime: Long)
    }

}
