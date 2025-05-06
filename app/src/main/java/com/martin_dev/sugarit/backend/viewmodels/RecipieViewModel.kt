package com.martin_dev.sugarit.backend.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.backend.model.api.Spoonacular.Recipie
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipieViewModel: ViewModel()
{

    private val _recipies = MutableLiveData<List<Recipie>>()
    val recipies: LiveData<List<Recipie>> = _recipies


    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun searchByIngredients(ingredients: String, alergies: String)
    {
        viewModelScope.launch {
            try
            {
                val response = retrofit.create(SpoonAPIService::class.java).getRecipieByIngredient(ingredients, apiKey = BuildConfig.API_KEY, intolerances = alergies)
                _recipies.value = if (response.isSuccessful)
                    response.body()?.results ?: emptyList()
                else
                    emptyList()
            }
            catch (e: Exception)
            {
                _recipies.value = emptyList()
                Log.e("RecipieViewModel", "Error: ${e.message}")
            }
        }
    }
}