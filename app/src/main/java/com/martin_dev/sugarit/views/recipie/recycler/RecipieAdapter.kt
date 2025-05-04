package com.martin_dev.sugarit.views.recipie.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.martin_dev.sugarit.backend.model.api.Spoonacular.Recipie
import com.martin_dev.sugarit.databinding.ActivityItemRecipieBinding

class RecipieAdapter(private val recipies: List<Recipie>) : RecyclerView.Adapter<RecipieViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecipieViewHolder
    {
        val binding = ActivityItemRecipieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipieViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: RecipieViewHolder,
        position: Int
    ) {
        holder.binding.title.text = recipies[position].title
        holder.binding.nutrients.text = recipies[position].nutrition.nutrients.toString()
        // Cargar la imagen con Gidle
    }

    override fun getItemCount(): Int = recipies.size

}