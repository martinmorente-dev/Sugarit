package com.martin_dev.sugarit.views.food

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.recipe.ConfidenceRange95Percent
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
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
        val recipeName  = intent.getStringExtra("recipe_name") ?: ""
        Log.i("RECIPE_NAME", recipeName)
        val carbMax = intent.getDoubleExtra("carbsMax", 0.0)
        val imageUri = intent.getStringExtra("image_uri")
        val unit = intent.getStringExtra("carbsUnit").toString()

        binding.foodRecipieImage.load(imageUri)
        setTitle(recipeName)
        binding.nutrients.text = "Carbohidratos: ${carbMax} ${unit}"
    }

    private fun setTitle(recipeName: String)
    {
        if(recipeName.isNotEmpty()) translateRecipeName(recipeName){ translated -> binding.title.text = translated} else recipeName
    }

    private fun translateRecipeName(recipeName: String, name: (String) -> Unit){
        val translator = TranslaterEnToSp()
        translator.translate(recipeName) { translatedText ->
            name(translatedText ?: recipeName)
        }
    }
}