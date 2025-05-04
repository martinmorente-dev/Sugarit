package com.martin_dev.sugarit.backend.model.api.Spoonacular

data class RecipieResponse(
    var results: List<Recipie>,
    var totalResults: Int
)