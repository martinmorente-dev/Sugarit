package com.martin_dev.sugarit.views.recipie.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipie
import com.martin_dev.sugarit.databinding.ActivityItemRecipieBinding
import kotlin.math.roundToInt

class RecipieAdapter(private val onItemClick: (Recipie) -> Unit) : RecyclerView.Adapter<RecipieViewHolder>()
{

    private val recipies = mutableListOf<Recipie>()

    fun setRecipies(newRecipies: List<Recipie>)
    {
        recipies.clear()
        recipies.addAll(newRecipies)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipieViewHolder
    {
        val binding = ActivityItemRecipieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecipieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecipieViewHolder, position: Int)
    {
        holder.binding.title.text = recipies[position].title
        holder.binding.nutrients.text = recipies[position].nutrition.nutrients.joinToString(separator = "\n")
        {
            "${it.name}: ${it.amount.roundToInt()} ${it.unit}"
        }
        Glide.with(holder.binding.recipieImage.context).load(recipies[position].image).into(holder.binding.recipieImage)
        holder.binding.root.setOnClickListener {
            onItemClick(recipies[position])
        }
    }

    override fun getItemCount(): Int = recipies.size
}