package com.martin_dev.sugarit.backend.controller.recipie

import android.util.Log
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.backend.model.api.Spoonacular.RecipieResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipieController
{
    private fun getRetrofit(): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun serchByIngredients(query: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = getRetrofit().create(SpoonAPIService::class.java)
                    .getRecipieByIngredient(query, apiKey = BuildConfig.API_KEY)

                if (response.isSuccessful) {
                    val recipies = response.body()
                    Log.i("Recipies", recipies.toString())
                } else {
                    Log.e("API_ERROR", "Código: ${response.code()}, mensaje: ${response.message()}")
                }
            } catch (e: Exception) {
                Log.e("NETWORK_ERROR", "Excepción: ${e.localizedMessage}")
            }
        }
    }

}