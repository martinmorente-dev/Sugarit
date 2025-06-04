package com.martin_dev.sugarit.views.recipie.recycler.recipie_saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
import com.martin_dev.sugarit.databinding.ActivityItemRecipieSaveBinding

class RecipieUserAdapter(
    private val onDeleteClickListener: (Recipe) -> Unit,
    private val onItemClick: (Recipe) -> Unit
) : RecyclerView.Adapter<RecipieUserViewHolder>() {

    private val recipes = mutableListOf<Recipe>()

    fun setRecipies(newRecipies: List<Recipe>) {
        recipes.clear()
        recipes.addAll(newRecipies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipieUserViewHolder {
        val binding = ActivityItemRecipieSaveBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipieUserViewHolder(binding, onDeleteClickListener, onItemClick)
    }

    override fun onBindViewHolder(holder: RecipieUserViewHolder, position: Int) {
        holder.bind(recipes[position])
    }

    override fun getItemCount(): Int = recipes.size
}

