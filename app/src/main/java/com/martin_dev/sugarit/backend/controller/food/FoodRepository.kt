package com.martin_dev.sugarit.backend.controller.food

import android.util.Log
import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Food
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Nutrition

class FoodRepository(private val api: SpoonAPIService) {

    suspend fun searchFood(query: String): Food? {
        val response = api.getFood(food = query)
        return if (response.isSuccessful) {
            response.body()?.results?.firstOrNull()
        } else {
            Log.e("API_ERROR", "Error en searchFood: ${response.errorBody()?.string()}")
            null
        }
    }

    suspend fun getFoodNutrition(foodId: Int, amount: Int): Nutrition? {
        val response = api.getFoodNutrition(foodId, amount)
        return if (response.isSuccessful) {
            val nutrients = response.body()?.nutrition?.nutrients
            if (nutrients != null) {
                val filtered = nutrients.filter {
                    it.name.contains("carbo", ignoreCase = true) ||
                            it.name.contains("sugar", ignoreCase = true)
                }
                Nutrition(nutrients = filtered)
            } else null
        } else null
    }
}
