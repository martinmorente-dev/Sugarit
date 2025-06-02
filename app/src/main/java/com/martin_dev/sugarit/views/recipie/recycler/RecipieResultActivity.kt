package com.martin_dev.sugarit.views.recipie.recycler

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.backend.viewmodels.recipe.RecipieViewModel
import com.martin_dev.sugarit.databinding.ActivityRecipieResultBinding
import com.martin_dev.sugarit.views.recipie.UserRecipiesActivity
import com.martin_dev.sugarit.views.recipie.instructions.InstructionsRecipeActivity

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

    override fun onResume()
    {
        super.onResume()
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
            onItemClick = { recipe ->
               var intent = Intent(this, InstructionsRecipeActivity::class.java)
                intent.putExtra("urlImage",recipe.image)
                intent.putExtra("recipeName",recipe.title)
                intent.putExtra("recipeId",recipe.id)
                startActivity(intent)
            },
            onSaveClick = { recipe ->
                if (recipe.isSaved)
                {
                    viewModel.saveRecipe(recipe.id.toString())
                    viewModel.updateRecipieSavedState(recipe.id, true)
                    Toast.makeText(this,"Receta ${recipe.title} guardada",Toast.LENGTH_SHORT).show()
                }
                else
                {
                    viewModel.deleteRecipie(recipe.id.toString())
                    viewModel.updateRecipieSavedState(recipe.id, false)
                    Toast.makeText(this,"Receta ${recipe.title} eliminada",Toast.LENGTH_SHORT).show()
                }
            }
        )
        binding.listResults.layoutManager = LinearLayoutManager(this)
        binding.listResults.adapter = adapter
    }

  private fun observeRecycler()
    {
        viewModel.recipies.observe(this) { recipies ->
            if (recipies.isNullOrEmpty())
                startActivity(Intent(this, RecipeResultErrorActivity::class.java))
            else
                adapter.setRecipies(recipies)
        }
    }

    private fun searchRecipies()
    {
        val ingredients = intent.getStringExtra("ingredients") ?: ""
        val alergies = intent.getStringExtra("alergies") ?: ""
        viewModel.searchByIngredients(ingredients, alergies)
    }
}