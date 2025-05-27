package com.martin_dev.sugarit.backend.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Nutrient
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RecipieUserViewModel : ViewModel() {
    private val _recipies = MutableLiveData<List<Recipe>>()
    val recipiesData: LiveData<List<Recipe>> = _recipies

    fun fetchRecipies(recipieIds: List<String>) {
        val ids = recipieIds.joinToString(",")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.api.getRecipeBulk(ids = ids)
                val recipies = response.body()
                if (recipies != null && recipies.isNotEmpty()) {
                    translateRecipies(recipies) { translatedList ->
                        _recipies.postValue(translatedList)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        _recipies.postValue(emptyList())
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _recipies.postValue(emptyList())
                }
            }
        }
    }

    private fun translateRecipies(
        recipies: List<Recipe>,
        onAllTranslated: (List<Recipe>) -> Unit
    ) {
        val translater = TranslaterEnToSp()
        val translatedRecipes = mutableListOf<Recipe>()
        var completedRecipes = 0

        if (recipies.isEmpty()) {
            onAllTranslated(emptyList())
            return
        }

        recipies.forEach { recipe ->
            translater.translate(recipe.title) { translatedTitle ->
                val nutrients = recipe.nutrition.nutrients
                val translatedNutrients = mutableListOf<Nutrient>()
                var completedNutrients = 0

                if (nutrients.isEmpty()) {
                    translatedRecipes.add(
                        recipe.copy(title = translatedTitle ?: recipe.title)
                    )
                    completedRecipes++
                    if (completedRecipes == recipies.size) {
                        onAllTranslated(translatedRecipes)
                    }
                } else {
                    nutrients.forEach { nutrient ->
                        translater.translate(nutrient.name) { translatedNutrientName ->
                            translatedNutrients.add(nutrient.copy(name = translatedNutrientName ?: nutrient.name))
                            completedNutrients++
                            if (completedNutrients == nutrients.size) {
                                translatedRecipes.add(
                                    recipe.copy(
                                        title = translatedTitle ?: recipe.title,
                                        nutrition = recipe.nutrition.copy(nutrients = translatedNutrients)
                                    )
                                )
                                completedRecipes++
                                if (completedRecipes == recipies.size) {
                                    onAllTranslated(translatedRecipes)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}