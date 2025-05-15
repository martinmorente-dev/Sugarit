package com.martin_dev.sugarit.views.food

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import com.martin_dev.sugarit.backend.controller.food.FoodRepository
import com.martin_dev.sugarit.backend.controller.food.FoodViewModel
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.martin_dev.sugarit.databinding.ActivityItemCameraResultBinding
import com.martin_dev.sugarit.databinding.ActivityItemRecipieSaveBinding
import kotlin.math.roundToInt

class ItemCameraResultActivity : AppCompatActivity()
{
    private lateinit var  binding: ActivityItemCameraResultBinding
    private lateinit var viewModel: FoodViewModel
    private val api = Retrofit.api
    private lateinit var repository: FoodRepository

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityItemCameraResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
        repository = FoodRepository(api)
        viewModel = ViewModelProvider(this)[FoodViewModel::class.java]
        viewModel.foodRepository = repository
        innitObservers()
        putImage()
    }

    private fun innitObservers()
    {
        viewModel.food.observe(this) { food ->
            binding.title.text = food?.name.toString()
        }
        viewModel.nutrition.observe(this) { nutrition ->
            val nutrientsText = nutrition?.nutrients?.joinToString("\n"){ nutrient ->
                "${nutrient.name}: ${nutrient.amount.roundToInt()} ${nutrient.unit}"
            } ?: "No nutrition Data"
            binding.nutrients.text = nutrientsText
        }
    }

    private fun putImage()
    {
        val imageUri = intent.getStringExtra("imageUri")
        if (imageUri != null)
        {
            val uri = imageUri.toUri()
            binding.foodRecipieImage.setImageURI(uri)
        }
    }
}