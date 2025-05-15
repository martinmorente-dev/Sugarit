package com.martin_dev.sugarit.backend.controller.food

import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Food
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Nutrition

class FoodRepository(private val api: SpoonAPIService)
{
    suspend fun searchFood(query: String) : Food?
    {
        val response = api.getFood(query)
        return if(response.isSuccessful) response.body() else null
    }

    suspend fun getFoodNutrition(foodId: Int, amount: Int): Nutrition?
    {
        val response = api.getFoodNutrition(foodId, amount)
        return if (response.isSuccessful) {
            val nutrients = response.body()?.nutrients
            if (nutrients != null) {
                val filtered = nutrients.filter { it.name == "Net Carbohydrates" || it.name == "Sugar" }
                Nutrition(nutrients = filtered)
            }
            else
                null
        }
        else
            null
    }
}