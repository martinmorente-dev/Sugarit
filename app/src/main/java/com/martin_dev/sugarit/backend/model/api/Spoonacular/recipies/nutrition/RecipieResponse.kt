package com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition

data class RecipieResponse(
    var results: List<Recipe>,
    var totalResults: Int
)