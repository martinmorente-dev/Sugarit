package com.martin_dev.sugarit.views.recipie.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
import com.martin_dev.sugarit.databinding.ActivityItemRecipieBinding
import kotlin.math.roundToInt
import com.martin_dev.sugarit.R

class RecipieAdapter(
    private val onItemClick: (Recipe) -> Unit,
    private val onSaveClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipieViewHolder>() {

    private val recipies = mutableListOf<Recipe>()

    fun setRecipies(newRecipies: List<Recipe>) {
        recipies.clear()
        recipies.addAll(newRecipies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipieViewHolder {
        val binding = ActivityItemRecipieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipieViewHolder(binding)
    }
    override fun onBindViewHolder(holder: RecipieViewHolder, position: Int) {
        val recipe = recipies[position]
        holder.binding.title.text = recipe.title

        val nutrientes = listOfNotNull(
            recipe.carbs?.let { "Carbohidratos: ${it.roundToInt()} g" },
            recipe.sugar?.let { "Azúcar: ${it.roundToInt()} g" }
        )

        holder.binding.nutrients.text = nutrientes.joinToString("\n").ifEmpty {
            "Sin datos de carbohidratos ni azúcar"
        }

        Glide.with(holder.binding.recipieImage.context).load(recipe.image).into(holder.binding.recipieImage)
        holder.binding.root.setOnClickListener { onItemClick(recipe) }
        holder.binding.saveRecipeBtn.setImageResource(
            if (recipe.isSaved) R.drawable.bookmark_filled else R.drawable.save_img
        )
        holder.binding.saveRecipeBtn.setOnClickListener {
            recipe.isSaved = !recipe.isSaved
            notifyItemChanged(position)
            onSaveClick(recipe)
        }
    }

    override fun getItemCount(): Int = recipies.size
}
