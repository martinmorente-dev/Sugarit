package com.martin_dev.sugarit.backend.controller.recipie

import android.util.Log
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.views.recipie.recycler.RecipieAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipieController
{
    private lateinit var recipieAdapter : RecipieAdapter

    private fun getRetrofit(): Retrofit
    {
        return Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun serchByIngredients(ingredients: String,intolerances: String)
    {
        CoroutineScope(Dispatchers.IO).launch {
            try
            {
                val response = getRetrofit().create(SpoonAPIService::class.java)
                    .getRecipieByIngredient(ingredients, apiKey = BuildConfig.API_KEY, intolerances = intolerances)
                Log.i("URL", response.raw().request.url.toString())
                if (response.isSuccessful)
                {
                    response.body()?.results?.let { recipies ->
                        withContext(Dispatchers.Main)
                        {
                            Log.i("API_RESPONSE",response.body().toString())
                            recipieAdapter.setRecipies(recipies)
                        }
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