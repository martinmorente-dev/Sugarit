package com.martin_dev.sugarit.backend.controller.mlkit

import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage


class ImageDetectorController(var imageSended: Bitmap, var context: Context)
{
    private val image = InputImage.fromBitmap(imageSended, 0)

    private val options = ObjectDetectorOptions.Builder()
        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
        .enableClassification()
        .build()

    private val objectDetector = ObjectDetection.getClient(options)

    fun recognizeFood()
    {
        objectDetector.process(image).addOnSuccessListener { detectedObjects ->
            for (detectedObject in detectedObjects)
            {
                if(detectedObject.labels.isNotEmpty())
                {
                    val label = detectedObject.labels.first()
                    if(label.text == "Food")
                    {
                        val confidence = label.confidence
                        if (confidence > 0.7f)
                            Log.i("Food Coincidence", "Food detected with confidence: $confidence")
                    }
                    else
                        AlertMessage().createAlert("No food detected", context)
                }
            }
        }
    }
}