package com.martin_dev.sugarit.backend.controller.api.spoonacular

import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Food
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Nutrition
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieResponse
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieSponnacular
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieUrl
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonAPIService
{
    @GET("recipes/complexSearch")
    suspend fun getRecipieByIngredient(
        @Query("includeIngredients") ingredients: String,
        @Query("maxSugar") maxSugar: Int = 5,
        @Query("maxCarbs") maxCarbs: Int = 20,
        @Query("intolerances") intolerances: String,
        @Query("apiKey") apiKey: String
    ): Response<RecipieResponse>

    @GET("recipes/{id}/information")
    suspend fun getRecipeURLByid(
        @Path("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Response<RecipieUrl>

    @GET("recipes/informationBulk")
    suspend fun getRecipeBulk(
        @Query("ids") ids: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Response<List<RecipieSponnacular>>

    @GET("food/ingredients/search")
    suspend fun getFood(
        @Query("query") food: String,
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY,
    ):Response<Food>

    @GET("food/ingredients/{id}/information")
    suspend fun getFoodNutrition(
        @Path("id") foodId: Int,
        @Query("amount") amount: Int,
        @Query("unit") unit: String = "medium",
        @Query("apiKey") apiKey: String = BuildConfig.API_KEY
    ): Response<Nutrition>
}