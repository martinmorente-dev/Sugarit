package com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies

data class Recipe(
    val id: Int,
    val title: String,
    val image: String,
    val nutrition: Nutrition,
    var isSaved: Boolean = false
)
