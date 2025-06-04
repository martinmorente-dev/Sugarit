package com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition

data class DetailedRecipe(
    val id: Int,
    val title: String,
    val image: String,
    val nutrition: Nutrition
)