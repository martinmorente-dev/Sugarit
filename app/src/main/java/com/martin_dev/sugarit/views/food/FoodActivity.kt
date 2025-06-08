package com.martin_dev.sugarit.views.food

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.martin_dev.sugarit.backend.controller.food.FoodController
import com.martin_dev.sugarit.backend.viewmodels.food.FoodViewModel
import com.martin_dev.sugarit.backend.utilites.alert_prompt.AlertPrompt
import com.martin_dev.sugarit.databinding.ActivityFoodBinding
import java.io.File

class FoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFoodBinding
    private lateinit var foodController: FoodController
    private lateinit var alertPrompt: AlertPrompt
    private var lastFoodQuantity: Int = 1
    private lateinit var imageUri: Uri
    private lateinit var viewModel: FoodViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        foodController = FoodController(this)
        viewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        alertPrompt = AlertPrompt()
        foodController.requestPermissionLauncher()
        innitListenners()
    }

    private fun innitListenners() {
        binding.camera.setOnClickListener {
            foodController.requestPermission()
        }
    }

    fun onPhotoTaken() {
        alertPrompt.createAlertPrompt("Recipe or Food", this) { foodOrRecipe ->
            if (foodOrRecipe.equals("ingredient", ignoreCase = true)) {
                alertPrompt.createAlertPrompt("Food type", this) { foodType ->
                    alertPrompt.createAlertPrompt("Food quantity", this) { foodQuantity ->
                        imageUri = getImage()
                        lastFoodQuantity = foodQuantity.toIntOrNull() ?: 1
                        viewModel.searchFood(foodType)
                        viewModel.food.observe(this) { food ->
                            food?.let {
                                val intent = Intent(this, ItemCameraResultActivity::class.java).apply {
                                    putExtra("food_name", it.name)
                                    putExtra("image_uri", imageUri.toString())
                                    putExtra("food_id", it.id)
                                    putExtra("food_quantity", lastFoodQuantity)
                                }
                                startActivity(intent)
                            }
                        }
                    }
                }
            }
            else if (foodOrRecipe.equals("recipe", ignoreCase = true))
            {
                alertPrompt.createAlertPrompt("Recipe Type", this){ recipeType ->
                    imageUri = getImage()
                    Log.i("IMAGE_URI","$imageUri")
                    Log.i("FLOWFood", "Antes de llamar al fetchRecipeNutrition")
                    viewModel.fetchRecipeNutrition(recipeType)
                    viewModel.recipeName.observe(this) { recipe ->
                        recipe?.let{
                            val carbMax = recipe.confidenceRange95Percent.max
                            val carbUnit = recipe.unit

                            val intent = Intent(this, RecipeNutritionActivity::class.java).apply {
                                putExtra("recipe_name", recipeType)
                                putExtra("carbsMax", carbMax)
                                putExtra("carbsUnit", carbUnit)
                                putExtra("image_uri", imageUri.toString())
                            }
                            startActivity(intent)
                        }

                    }
                }
            }
        }
    }

    private fun getImage(): Uri
    {
        val file = File(this.cacheDir, "images/image.jpg")
        file.parentFile?.mkdirs()
        return FileProvider.getUriForFile(this, "${this.packageName}.fileprovider", file)
    }
}