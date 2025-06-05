package com.martin_dev.sugarit.backend.controller.mlkit

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import androidx.core.content.FileProvider
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.views.food.FoodActivity
import java.io.File

class ImageDetectorController(
    private val imageSended: Bitmap,
    private val context: Context
) {
    private val image = InputImage.fromBitmap(imageSended, 0)
    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableClassification()
        .build()
    private val objectDetector = ObjectDetection.getClient(options)

    fun recognizeFood() {
        objectDetector.process(image).addOnSuccessListener { detectedObjects ->
            if (detectedObjects.isEmpty()) {
                AlertMessage().createAlert("Image not detected", context)
                return@addOnSuccessListener
            }
            for (detectedObject in detectedObjects) {
                if (detectedObject.labels.isNotEmpty()) {
                    val label = detectedObject.labels.first()
                    if (label.text == "Food" && label.confidence > 0.7f)
                    {
                        saveBitmapToCache()
                        (context as? FoodActivity)?.let { activity ->
                            activity.onPhotoTaken()
                        }
                    } else
                        AlertMessage().createAlert("Image not detected", context)
                } else {
                    AlertMessage().createAlert("Image not detected", context)
                }
            }
        }
    }

    private fun saveBitmapToCache(): Uri {
        val cachePath = File(context.cacheDir, "images")
        cachePath.deleteRecursively()
        cachePath.mkdirs()
        val file = File(cachePath, "image.jpg")
        val stream = file.outputStream()
        imageSended.compress(Bitmap.CompressFormat.JPEG, 90, stream)
        stream.close()
        return FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", file
        )
    }
}
