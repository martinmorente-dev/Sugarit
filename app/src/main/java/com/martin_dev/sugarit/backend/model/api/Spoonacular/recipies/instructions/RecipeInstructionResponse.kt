package com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.instructions

data class RecipeInstructionResponse(
    val cookingMinutes: Int,
    val extendedIngredients: List<ExtendedIngredients>,
    val analyzedInstructions: List<AnalyzedInstructions>
)