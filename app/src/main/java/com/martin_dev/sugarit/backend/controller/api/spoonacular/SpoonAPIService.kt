package com.martin_dev.sugarit.backend.controller.api.spoonacular

import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieResponse
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieUrl
import retrofit2.Response
import retrofit2.http.GET
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
        @Query("id") recipeId: Int,
        @Query("apiKey") apiKey: String
    ): Response<RecipieUrl>
}