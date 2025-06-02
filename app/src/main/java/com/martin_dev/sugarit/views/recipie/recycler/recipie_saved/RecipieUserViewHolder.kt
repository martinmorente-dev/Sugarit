package com.martin_dev.sugarit.views.recipie.recycler.recipie_saved

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition.Recipe
import com.martin_dev.sugarit.databinding.ActivityItemRecipieSaveBinding
import kotlin.math.roundToInt

class RecipieUserViewHolder(
    private val binding: ActivityItemRecipieSaveBinding,
    private val onDeleteClickListener: (Recipe) -> Unit,
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(recipe: Recipe) {
        binding.title.text = recipe.title
        Glide.with(binding.recipieImage.context).load(recipe.image).into(binding.recipieImage)

        val carbs = recipe.nutrition.nutrients.find {
            it.name.equals("Carbohidratos netos", ignoreCase = true)
        }
        val sugar = recipe.nutrition.nutrients.find {
            it.name.equals("Azúcar", ignoreCase = true)
        }

        binding.nutrients.text = "Carbs: ${carbs?.amount?.roundToInt() ?: 0} ${carbs?.unit ?: ""}\n" +
                "Sugar: ${sugar?.amount?.roundToInt() ?: 0} ${sugar?.unit ?: ""}"

        binding.erraseRecipieBtn.setOnClickListener {
            onDeleteClickListener(recipe)
        }
        binding.root.setOnClickListener {
            onItemClick(recipe)
        }
    }
}

