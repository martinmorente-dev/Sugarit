package com.martin_dev.sugarit.backend.controller.camera

import androidx.activity.result.ActivityResultLauncher
import android.Manifest
import android.content.Intent
import android.provider.MediaStore
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage


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
            Log.i("data image", data?.extras.toString())
        }
    }

    fun requestPermission()
    {
        cameraPermission.launch(CAMERA_PERMISSON)
    }

    private fun openCamera()
    {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (takePictureIntent.resolveActivity(activity.packageManager) != null)
            cameraLauncher.launch(takePictureIntent)
    }

}