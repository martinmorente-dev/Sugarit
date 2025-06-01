package com.martin_dev.sugarit.views.recipie.recycler

import android.util.Log
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

        Log.d("RecipieAdapter", "Nutrientes: " + recipe.nutrition.nutrients.joinToString { it.name })

        val nutrientesFiltrados = recipe.nutrition.nutrients.filter {
            val nombre = it.name.lowercase()
            nombre == "los hidratos de carbono" ||
                    nombre == "azúcar"
        }

        holder.binding.nutrients.text = nutrientesFiltrados.joinToString(separator = "\n") {
            "${it.name}: ${it.amount.roundToInt()} ${it.unit}"
        }.ifEmpty {
            "Sin datos de carbohidratos ni azúcar"
        }

        Glide.with(holder.binding.recipieImage.context).load(recipe.image).into(holder.binding.recipieImage)
        holder.binding.root.setOnClickListener {
            onItemClick(recipe)
        }
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
