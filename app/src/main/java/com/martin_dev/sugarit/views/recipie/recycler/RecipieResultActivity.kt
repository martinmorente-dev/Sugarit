package com.martin_dev.sugarit.views.recipie.recycler

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.viewmodels.RecipieViewModel
import com.martin_dev.sugarit.databinding.ActivityRecipieResultBinding

class RecipieResultActivity() : AppCompatActivity()
{
    private lateinit var binding: ActivityRecipieResultBinding
    private lateinit var adapter: RecipieAdapter
    private val viewModel: RecipieViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = RecipieAdapter()
        binding.listResults.layoutManager = LinearLayoutManager(this)
        binding.listResults.adapter = adapter

        val ingredients = intent.getStringExtra("ingredients") ?: ""
        val alergies = intent.getStringExtra("alergies") ?: ""

        viewModel.searchByIngredients(ingredients, alergies)

        // Observe the LiveData and update the adapter
        viewModel.recipies.observe(this) { recipies ->
            if (recipies.isEmpty()) {
                AlertMessage().createAlert("No results found", this)
                Log.i("RecipieResultActivity AA", "No results found")
            }
            adapter.setRecipies(recipies)
        }
    }
}