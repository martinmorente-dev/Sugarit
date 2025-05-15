package com.martin_dev.sugarit.views.food

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.controller.food.FoodController
import com.martin_dev.sugarit.backend.utilites.alert_prompt.AlertPrompt
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.databinding.ActivityFoodBinding

class FoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var foodController: FoodController
    private lateinit var alertPrompt: AlertPrompt

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foodController = FoodController(this)
        alertPrompt = AlertPrompt()
        foodController.requestPermissionLauncher()
        innitListenners()
    }

    private fun innitListenners()
    {
        binding.camera.setOnClickListener {
            foodController.requestPermission()
        }
    }

    fun onPhotoTaken() {
        alertPrompt.createAlertPrompt("Recipie or Food", this) { foodOrRecipie ->
            Log.i("Food or Recipe",foodOrRecipie)
            if(foodOrRecipie == "") AlertMessage().createAlert("Wrong answer", this)
            if(foodOrRecipie == "meal") {
                alertPrompt.createAlertPrompt("Food type", this) { foodType ->
                    Log.i("Food Type", foodType)
                    alertPrompt.createAlertPrompt("Food quantity", this) { foodQuantity ->
                        Log.i("Food Quantity", foodQuantity)
                        startActivity(Intent(this, ItemCameraResultActivity::class.java))
                    }
                }
            }
            else
                Log.i("Recipie", foodOrRecipie)
        }
    }
}