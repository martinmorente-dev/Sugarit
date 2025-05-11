package com.martin_dev.sugarit.views.recipie.recycler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.backend.viewmodels.RecipieViewModel
import com.martin_dev.sugarit.databinding.ActivityRecipieResultBinding
import com.martin_dev.sugarit.backend.controller.Recipe.RecipieController
import com.martin_dev.sugarit.views.recipie.UserRecipiesActivity

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
        innitListeners()
        observeRecycler()
        searchRecipies()
    }

    fun innitListeners()
    {
        binding.userRecipiesBtn.setOnClickListener {
            startActivity(Intent(this, UserRecipiesActivity::class.java))
        }
    }

    fun innitRecycler()
    {
        adapter = RecipieAdapter(
            onItemClick = { recipie ->
                RecipieController(this, this).searchRecipieUrl(recipie.id)
            },
            onSaveClick = { recipie ->
                RecipieController(this, this).saveRecipe(recipie.id.toString())
                Toast.makeText(this, "Recipe saved", Toast.LENGTH_SHORT).show()
            }
        )
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