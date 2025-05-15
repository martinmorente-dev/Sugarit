package com.martin_dev.sugarit.backend.controller.food

import androidx.activity.result.ActivityResultLauncher
import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import com.martin_dev.sugarit.backend.controller.mlkit.ImageDetectorController
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.views.food.FoodActivity


class FoodController(private val activity: AppCompatActivity)
{
    private lateinit var cameraPermission: ActivityResultLauncher<String>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private val CAMERA_PERMISSON = Manifest.permission.CAMERA


    fun requestPermissionLauncher()
    {
        cameraPermission = activity.registerForActivityResult(ActivityResultContracts.RequestPermission()){ isGranted ->
            if (isGranted) openCamera()
            else AlertMessage().createAlert("Permission not granted", activity)
        }

        cameraLauncher = activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val data = result.data
            val extras = data?.extras
            val bitmap = extras?.get("data") as? Bitmap
            if(bitmap != null) {
                ImageDetectorController(bitmap, activity).recognizeFood()
            }
            else
                Toast.makeText(activity, "No se pudo obtener la imagen", Toast.LENGTH_SHORT).show()
        }
    }

    fun requestPermission()
    {
        cameraPermission.launch(CAMERA_PERMISSON)
    }

    private fun openCamera()
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity.packageManager) != null) {
            cameraLauncher.launch(takePictureIntent)
        }
        else
            Toast.makeText(activity, "Unable to open camera", Toast.LENGTH_SHORT).show()
    }
}