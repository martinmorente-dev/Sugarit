package com.martin_dev.sugarit.backend.model.api.Spoonacular

data class Recipie(
    val title: String,
    val image: String,
    val nutrition: Nutrition
)
