package com.martin_dev.sugarit.views.recipie.instructions

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ImageSpan
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.instructions.RecipeInstructionResponse
import com.martin_dev.sugarit.backend.viewmodels.recipe.instructions.RecipeInstructionsViewModel
import com.martin_dev.sugarit.databinding.ActivityInstructionsRecipeBinding
import androidx.core.graphics.toColorInt
import androidx.core.graphics.createBitmap
import com.martin_dev.sugarit.views.recipie.recycler.RecipieResultActivity

class InstructionsRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstructionsRecipeBinding
    private val viewModel: RecipeInstructionsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionsRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        innitListeners()
        observeRecipeInstructions()
        errorMessageHandler()
        loadRecipe()
    }

    private fun innitListeners() {
        binding.backBtn.bringToFront()
        binding.backBtn.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun observeRecipeInstructions() {
        viewModel.recipeInstructions.observe(this) { recipe ->
            drawRecipe(recipe)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun drawRecipe(recipe: RecipeInstructionResponse) {
        binding.title.text = intent.getStringExtra("recipeName")
        Glide.with(this)
            .load(intent.getStringExtra("urlImage")?.toUri())
            .into(binding.recipeImage)

        val minutes = recipe.cookingMinutes?.takeIf { it > 0 }
            ?: recipe.readyInMinutes?.takeIf { it > 0 }
        binding.time.text = "${minutes?.toString()} min"

        binding.recipeIngredients.text = ""
        var counter = 1
        recipe.extendedIngredients.forEach { extendedIngredient ->
            val roundedNumber = getCircleNumberSpan(counter)
            binding.recipeIngredients.append(roundedNumber)
            binding.recipeIngredients.append("   ")
            binding.recipeIngredients.append("${extendedIngredient.nameClean} ${extendedIngredient.measures.metric.amount} ${extendedIngredient.measures.metric.unitShort}\n\n")
            counter++
        }

        binding.recipeInstructions.text = ""
        recipe.analyzedInstructions.forEach { analyzedInstruction ->
            analyzedInstruction.steps.forEach { step ->
                val roundedStep = getCircleNumberSpan(step.number)
                binding.recipeInstructions.append(roundedStep)
                binding.recipeInstructions.append("   ")
                binding.recipeInstructions.append("${step.step}\n\n")
            }
        }
    }

    private fun getCircleNumberSpan(number: Int): SpannableString {
        val sizePx = 60
        val text = number.toString()
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        paint.textSize = 32f
        paint.color = "#333333".toColorInt()
        paint.textAlign = Paint.Align.CENTER

        val bitmap = createBitmap(sizePx, sizePx)
        val canvas = Canvas(bitmap)

        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG)
        circlePaint.color = "#E0E0E0".toColorInt()
        canvas.drawOval(RectF(0f, 0f, sizePx.toFloat(), sizePx.toFloat()), circlePaint)

        val x = sizePx / 2f
        val y = sizePx / 2f - (paint.descent() + paint.ascent()) / 2
        canvas.drawText(text, x, y, paint)

        val spannable = SpannableString("  $text  ")
        val imageSpan = ImageSpan(this, bitmap, ImageSpan.ALIGN_BOTTOM)
        spannable.setSpan(imageSpan, 0, spannable.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return spannable
    }

    private fun loadRecipe() {
        val recipeId: Int = intent.getIntExtra("recipeId", 0)
        viewModel.fetchRecipeInstructions(recipeId = recipeId)
    }

    private fun errorMessageHandler() {
        viewModel.errorMessage.observe(this) { errorMessage ->
            val errorMessage = when {
                errorMessage.startsWith("Quota exceeded") -> "Cupo de cuotas excedido"
                errorMessage.startsWith("Error Call") -> "Error en la llamada: ${errorMessage.substringAfter(":").trim()}"
                errorMessage.startsWith("Error Network") -> "Error de red: ${errorMessage.substringAfter(":").trim()})"
                else -> "Error desconocido: ${errorMessage.substringAfter(":").trim()}"
            }
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}