package com.martin_dev.sugarit.views.food

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.controller.food.FoodViewModel
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import com.martin_dev.sugarit.databinding.ActivityItemCameraResultBinding
import kotlin.math.roundToInt

class ItemCameraResultActivity : AppCompatActivity() {
    private lateinit var binding: ActivityItemCameraResultBinding
    private lateinit var translater: TranslaterEnToSp
    private var translatedFoodName: String? = null
    private val pendingTranslations = mutableListOf<String>()
    private var completedTranslations = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        translater = TranslaterEnToSp()

        // Obtener datos del intent
        val foodId = intent.getIntExtra("food_id", -1)
        val foodQuantity = intent.getIntExtra("food_quantity", 1)
        val foodName = intent.getStringExtra("food_name") ?: ""

        // Traducir nombre del alimento
        translateFoodName(foodName)

        val viewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        if (foodId != -1 && foodId != 0) {
            Log.i("ID_FOOD","$foodId")
            viewModel.fetchFoodNutrition(foodId, foodQuantity)
            viewModel.nutrition.observe(this) { nutrition ->
                nutrition?.nutrients?.let { nutrients ->
                    translateNutrients(nutrients)
                } ?: run {
                    binding.nutrients.text = "No hay datos nutricionales"
                }
            }
        } else {
            binding.nutrients.text = "Error: ID no válido"
        }
        putImage()
    }

    private fun translateFoodName(originalName: String) {
        translater.translate(originalName) { translated ->
            translatedFoodName = translated ?: originalName
            binding.title.text = translatedFoodName
        }
    }

    private fun translateNutrients(nutrients: List<com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Nutrient>) {
        val translatedNutrients = mutableListOf<String>()
        pendingTranslations.clear()
        completedTranslations = 0

        nutrients.forEach { nutrient ->
            pendingTranslations.add(nutrient.name)
            translater.translate(nutrient.name) { translatedName ->
                synchronized(this) {
                    val finalName = translatedName ?: nutrient.name
                    translatedNutrients.add("$finalName: ${nutrient.amount.roundToInt()} ${nutrient.unit}")
                    completedTranslations++

                    if (completedTranslations == pendingTranslations.size) {
                        runOnUiThread {
                            binding.nutrients.text = translatedNutrients.joinToString("\n")
                        }
                    }
                }
            }
        }
    }


    private fun putImage() {
        val imageUriString = intent.getStringExtra("image_uri")
        if (imageUriString != null) {
            val uri = Uri.parse(imageUriString)
            Glide.with(this)
                .load(uri)
                .into(binding.foodRecipieImage)
        }
    }

    override fun onDestroy()
    {
        super.onDestroy()
    }
}
