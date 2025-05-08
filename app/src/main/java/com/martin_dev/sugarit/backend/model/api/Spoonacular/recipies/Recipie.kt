package com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies

data class Recipie(
    val id: Int,
    val title: String,
    val image: String,
    val nutrition: Nutrition
)
