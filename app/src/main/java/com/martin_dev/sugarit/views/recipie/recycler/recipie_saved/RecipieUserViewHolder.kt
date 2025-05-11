package com.martin_dev.sugarit.views.recipie.recycler.recipie_saved

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieSponnacular
import com.martin_dev.sugarit.databinding.ActivityItemRecipieSaveBinding
import kotlin.math.roundToInt

class RecipieUserViewHolder(
    private val binding: ActivityItemRecipieSaveBinding,
    private val onDeleteClickListener: (RecipieSponnacular) -> Unit,
    private val onItemClick: (RecipieSponnacular) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(recipe: RecipieSponnacular) {
        binding.title.text = recipe.title
        Glide.with(binding.recipieImage.context).load(recipe.image).into(binding.recipieImage)

        val carbs = recipe.nutrition.nutrients.find {
            Log.i("RecipieUserViewHolder", "name: ${it.name}")
            it.name.equals("Carbohydrates", ignoreCase = true) ||
                    it.name.equals("Carbohidratos netos", ignoreCase = true) ||
                    it.name.equals("Carbohidratos", ignoreCase = true)
        }
        val sugar = recipe.nutrition.nutrients.find {
            it.name.equals("Sugar", ignoreCase = true) ||
                    it.name.equals("Azúcar", ignoreCase = true) ||
                    it.name.equals("Azucar", ignoreCase = true)
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
