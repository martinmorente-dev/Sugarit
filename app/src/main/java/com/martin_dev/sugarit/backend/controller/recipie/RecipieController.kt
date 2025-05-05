package com.martin_dev.sugarit.backend.controller.recipie

import android.util.Log
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.backend.model.api.Spoonacular.RecipieResponse
import com.martin_dev.sugarit.views.recipie.recycler.RecipieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    fun serchByIngredients(ingredients: String,intolerances: String): List<RecipieResponse>
    {
        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val response = getRetrofit().create(SpoonAPIService::class.java)
                    .getRecipieByIngredient(ingredients, apiKey = BuildConfig.API_KEY, intolerances = intolerances)
                Log.i("URL", response.raw().request.url.toString())
                if (response.isSuccessful)
                {
                    val recipies = response.body()?.results ?: emptyList()
                    withContext(Dispatchers.Main) {
                        return@withContext (recipies)
                    }
                }
                else
                {
                    val error = response.errorBody()?.string()
                    Log.e("API_ERROR", "$error")
                }
            }
            catch (e: Exception)
            {
                Log.e("NETWORK_ERROR", "Excepción: ${e.localizedMessage}")
            }
        }
    }

}