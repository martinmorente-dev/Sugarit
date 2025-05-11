package com.martin_dev.sugarit.backend.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Nutrient
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieSponnacular
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieResponse
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import kotlinx.coroutines.launch
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit

class RecipieViewModel: ViewModel()
{

    private val _recipies = MutableLiveData<List<RecipieSponnacular>>()
    val recipies: LiveData<List<RecipieSponnacular>> = _recipies

    fun searchByIngredients(ingredients: String, alergies: String)
    {
        viewModelScope.launch {
            try
            {
                val response = Retrofit.api.getRecipieByIngredient(ingredients, apiKey = BuildConfig.API_KEY, intolerances = alergies)
                if (response.isSuccessful)
                {
                    val body = response.body()
                    if(body != null)
                    {
                        translatedList(listOf(body)) { onAllTranslated ->
                            val traducedRecipies = onAllTranslated.flatMap { it.results }
                            _recipies.postValue(traducedRecipies)
                        }
                    }
                    else
                        _recipies.value = emptyList()
                }
                else
                    _recipies.value = emptyList()
            }
            catch (e: Exception)
            {
                _recipies.value = emptyList()
                Log.e("RecipieViewModel", "Error: ${e.message}")
            }
        }
    }

    fun translatedList(apiResults: List<RecipieResponse>, onAllTranslated: (List<RecipieResponse>) -> Unit) {

        val translater = TranslaterEnToSp()
        val translatedRecipes = mutableListOf<RecipieSponnacular>()
        val allRecipes = apiResults.flatMap { it.results }
        var completedRecipes = 0

        allRecipes.forEach { recipe ->
            translater.translate(recipe.title) { translatedTitle ->
                val nutrients = recipe.nutrition.nutrients
                val translatedNutrients = mutableListOf<Nutrient>()
                var completedNutrients = 0

                nutrients.forEach { nutrient ->
                    translater.translate(nutrient.name) { nutrientsName ->
                        translatedNutrients.add(nutrient.copy(name = nutrientsName ?: nutrient.name))
                        completedNutrients++
                        if (completedNutrients == nutrients.size) {
                            val translatedRecipe = recipe.copy(
                                title = translatedTitle ?: recipe.title,
                                nutrition = recipe.nutrition.copy(nutrients = translatedNutrients)
                            )
                            translatedRecipes.add(translatedRecipe)
                            completedRecipes++
                            if (completedRecipes == allRecipes.size) {
                                onAllTranslated(
                                    listOf(
                                        RecipieResponse(
                                            results = translatedRecipes,
                                            totalResults = translatedRecipes.size
                                        )
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}