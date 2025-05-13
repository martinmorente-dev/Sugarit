package com.martin_dev.sugarit.views.food

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.controller.camera.FoodController
import com.martin_dev.sugarit.databinding.ActivityFoodBinding

class FoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var foodController: FoodController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foodController = FoodController(this)
        foodController.requestPermissionLauncher()
        innitListenners()
    }

    private fun innitListenners()
    {
        binding.camera.setOnClickListener {
            foodController.requestPermission()
        }
    }
}