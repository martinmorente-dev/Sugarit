package com.martin_dev.sugarit.views.recipie.recycler

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.viewmodels.RecipieViewModel
import com.martin_dev.sugarit.databinding.ActivityRecipieResultBinding
import com.martin_dev.sugarit.views.recipie.RecipieInstrucctions

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
        innitRecycler()
        observeRecycler()
        searchRecipies()
    }

    fun innitRecycler()
    {
        adapter = RecipieAdapter { recipie ->
            Log.i("RecipieID","${recipie.id}")
            Log.i("ID_RecipieResultActivity","${ recipie.id }")
            RecipieInstrucctions(this,this).searchRecipieUrl(recipie.id)
        }
        binding.listResults.layoutManager = LinearLayoutManager(this)
        binding.listResults.adapter = adapter
    }

    fun observeRecycler()
    {
        viewModel.recipies.observe(this) { recipies ->
            if (recipies.isEmpty()) {
                AlertMessage().createAlert("No results found", this)
                Log.i("RecipieResultActivity", "No results found")
            }
            adapter.setRecipies(recipies)
        }
    }

    fun searchRecipies()
    {
        val ingredients = intent.getStringExtra("ingredients") ?: ""
        val alergies = intent.getStringExtra("alergies") ?: ""
        viewModel.searchByIngredients(ingredients, alergies)
    }
}