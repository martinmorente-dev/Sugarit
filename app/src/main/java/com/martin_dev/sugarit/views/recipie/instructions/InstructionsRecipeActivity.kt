package com.martin_dev.sugarit.views.recipie.instructions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.instructions.RecipeInstructionResponse
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.backend.viewmodels.recipe.instructions.RecipeInstructionsViewModel
import com.martin_dev.sugarit.databinding.ActivityInstructionsRecipeBinding
import com.martin_dev.sugarit.views.recipie.RecipieActivity

class InstructionsRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstructionsRecipeBinding
    private val viewModel: RecipeInstructionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionsRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        innitListenners()
        observeRecipeInstructions()
        errorMessageHandler()
        loadRecipe()
    }

    private fun innitListenners()
    {
        binding.backBtn.bringToFront()
        binding.backBtn.setOnClickListener{
            startActivity(Intent(this, RecipieActivity::class.java))
        }
    }


    private fun observeRecipeInstructions() {
        viewModel.recipeInstructions.observe(this) { recipe ->
            drawRecipe(recipe)
        }
    }

    private fun drawRecipe(recipe: RecipeInstructionResponse)
    {
        binding.title.text = intent.getStringExtra("recipeName")
        Glide.with(this)
            .load(intent.getStringExtra("urlImage")?.toUri())
            .into(binding.recipeImage)

        binding.time.text = recipe.cookingMinutes.toString()
        binding.recipeIngredients.text = ""

        var counter = 1
        recipe.extendedIngredients.forEach { extendedIngredient ->

            binding.recipeIngredients.append("$counter ${extendedIngredient.nameClean} ${extendedIngredient.measures.metric.amount} " +
                    "${extendedIngredient.measures.metric.unitShort} \n \n")
            counter++
        }

        recipe.analyzedInstructions.forEach { analyzedInstruction ->
            analyzedInstruction.steps.forEach { step ->
                binding.recipeInstructions.append("${step.number}. ${step.step} \n \n")
            }
        }

    }

    private fun loadRecipe() {
        val recipeId: Int = intent.getIntExtra("recipeId", 0)
        viewModel.fetchRecipeInstructions(recipeId = recipeId)
    }

    private fun errorMessageHandler()
    {
        viewModel.errorMessage.observe(this) { errorMessage ->

            val errorMessage =  when
            {
                errorMessage.startsWith("Quota exceeded") -> "Cupo de cuotas excedido"
                errorMessage.startsWith("Error Call") -> "Error en la llamada: ${errorMessage.substringAfter(":").trim()}"
                errorMessage.startsWith("Error Network") -> "Error de red: ${errorMessage.substringAfter(":").trim()})"
                else -> "Error desconocido: ${errorMessage.substringAfter(":").trim()}"
            }

            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}