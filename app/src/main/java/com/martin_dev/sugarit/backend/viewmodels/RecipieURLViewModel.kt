package com.martin_dev.sugarit.backend.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieUrl
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipieURLViewModel : ViewModel()
{
    private val _recipeurl = MutableLiveData<RecipieUrl>()
    val recipeurl: MutableLiveData<RecipieUrl> = _recipeurl

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(SpoonAPIService::class.java)

    fun searchRecipieUrl(recipeId: Int)
    {
        viewModelScope.launch {
            try
            {
                val response = api.getRecipeURLByid(apiKey = BuildConfig.API_KEY, recipeId = recipeId)
                Log.i("RecipieURLViewModel", "Response: $response")
                _recipeurl.postValue(response.body())
            }
            catch (e: Exception)
            {
                Log.e("ERROR_RecipieURLViewModel", "Error: ${e.message}")
                _recipeurl.postValue(RecipieUrl(null))
            }
        }
    }
}