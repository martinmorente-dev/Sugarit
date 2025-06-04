package com.martin_dev.sugarit.views.recipie.recycler.recipie_saved

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
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

        val carbsText = if (recipe.carbs != null) {
            "Carbs: ${recipe.carbs.roundToInt()} g"
        } else {
            "Carbs: Sin datos"
        }

        val sugarText = if (recipe.sugar != null) {
            "Sugar: ${recipe.sugar.roundToInt()} g"
        } else {
            "Sugar: Sin datos"
        }

        binding.nutrients.text = "$carbsText\n$sugarText"

        binding.erraseRecipieBtn.setOnClickListener {
            onDeleteClickListener(recipe)
        }
        binding.root.setOnClickListener {
            onItemClick(recipe)
        }
    }
}
