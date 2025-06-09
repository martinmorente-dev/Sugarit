package com.martin_dev.sugarit.views.food

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.martin_dev.sugarit.backend.viewmodels.food.FoodViewModel
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import com.martin_dev.sugarit.databinding.ActivityItemCameraResultBinding
import kotlin.math.roundToInt
import androidx.core.net.toUri
import com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.ingredient.Nutrient
import com.martin_dev.sugarit.backend.utilites.traductions.LibraryTraductions
import com.martin_dev.sugarit.views.utils.observeOnce

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

        val foodId = intent.getIntExtra("food_id", -1)
        val foodQuantity = intent.getIntExtra("food_quantity", 1)
        val foodName = intent.getStringExtra("food_name") ?: ""

        translateFoodName(foodName)

        val viewModel = ViewModelProvider(this)[FoodViewModel::class.java]

        if (foodId != -1 && foodId != 0) {
            Log.i("ID_FOOD","$foodId")
            viewModel.fetchFoodNutrition(foodId, foodQuantity)
            viewModel.nutrition.observeOnce (this) { nutrition ->
                nutrition?.nutrients?.let { nutrients ->
                    translateNutrients(nutrients)
                } ?: run {
                    binding.nutrients.text = "No hay datos nutricionales"
                }
            }
        }
        else
            binding.nutrients.text = "Error: ID no válido"
        putImage()
    }

    private fun translateFoodName(originalName: String) {
        val translations = LibraryTraductions().translations
        val translatedDirect = translations[originalName]
        Log.i("NAME", originalName)
        if (translatedDirect != null) {
            translatedFoodName = translatedDirect
            binding.title.text = translatedFoodName
        } else {
            translater.translate(originalName) { translated ->
                translatedFoodName = translated ?: originalName
                binding.title.text = translatedFoodName
            }
        }
    }

    private fun translateNutrients(nutrients: List<Nutrient>) {
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
            val uri = imageUriString.toUri()
            Glide.with(this)
                .load(uri)
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.foodRecipieImage)
        }
    }
}