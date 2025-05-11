package com.martin_dev.sugarit.views.recipie.recycler.recipie_saved

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieSponnacular
import com.martin_dev.sugarit.databinding.ActivityItemRecipieSaveBinding

class RecipieUserAdapter(
    private val onDeleteClickListener: (RecipieSponnacular) -> Unit,
    private val onItemClick: (RecipieSponnacular) -> Unit
) : RecyclerView.Adapter<RecipieUserViewHolder>() {

    private val recipes = mutableListOf<RecipieSponnacular>()

    fun setRecipies(newRecipies: List<RecipieSponnacular>) {
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
