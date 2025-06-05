package com.martin_dev.sugarit.views.food

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.recipe.ConfidenceRange95Percent
import com.martin_dev.sugarit.databinding.ActivityRecipeNutritionBinding

class RecipeNutritionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipeNutritionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeNutritionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        drawData()
        innitListenner()
    }

    private fun innitListenner()
    {
        binding.backBtn.setOnClickListener {
            finish()
        }
    }

    private fun drawData() {
        val recipeName = intent.getStringExtra("recipe_name")
        val carbs = intent.getParcelableExtra<ConfidenceRange95Percent>("carbs")
        val imageUri = intent.getStringExtra("image_uri")
        val unit = intent.getStringExtra("unit").toString()

        binding.foodRecipieImage.load(imageUri)
        binding.title.text = recipeName
        binding.nutrients.text = "${carbs} ${unit}"

    }
}