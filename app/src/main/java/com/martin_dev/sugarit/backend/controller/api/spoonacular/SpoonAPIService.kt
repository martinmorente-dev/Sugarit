package com.martin_dev.sugarit.backend.controller.api.spoonacular

import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.FoodResponse
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.NutritionResponse
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipeResponse
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.instructions.RecipeInstructionResponse
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition.DetailedRecipe
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonAPIService {

    @GET("recipes/findByIngredients")
    suspend fun getRecipeByIngredient(
        @Query("ingredients") ingredients: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("number") number: Int = 10,
        @Query("ranking") ranking: Int = 1,
    ):Response<List<RecipeResponse>>

    @GET("recipes/informationBulk")
    suspend fun getRecipeBulk(
        @Query("ids") ids: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Response<List<DetailedRecipe>>

    @GET("food/ingredients/search")
    suspend fun getFood(
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("query") food: String,
        @Query("number") number: Int = 1
    ): Response<FoodResponse>

    @GET("food/ingredients/{id}/information")
    suspend fun getFoodNutrition(
        @Path("id") foodId: Int,
        @Query("amount") amount: Int,
        @Query("unit") unit: String = "medium",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<NutritionResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeInstruction(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<RecipeInstructionResponse>
}