package com.martin_dev.sugarit.backend.viewmodels.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition.DetailedRecipe
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition.Nutrient
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
class RecipieUserViewModel : ViewModel() {
    private val _recipies = MutableLiveData<List<Recipe>>()
    val recipiesData: LiveData<List<Recipe>> = _recipies

    fun fetchRecipies(recipieIds: List<String>) {
        val ids = recipieIds.joinToString(",")
        Log.d("RecipieUserViewModel", "Buscando recetas con IDs: $ids")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = Retrofit.api.getRecipeBulk(ids = ids)
                val detailedRecipes = response.body()
                if (!detailedRecipes.isNullOrEmpty()) {
                    translateAndMapRecipes(detailedRecipes) { translatedList ->
                        _recipies.postValue(translatedList)
                    }
                }
                else
                    _recipies.postValue(emptyList())
            } catch (e: Exception) {
                _recipies.postValue(emptyList())
            }
        }
    }

    private fun translateAndMapRecipes(
        detailedRecipes: List<DetailedRecipe>,
        onAllTranslated: (List<Recipe>) -> Unit
    ) {
        val translater = TranslaterEnToSp()
        val translatedRecipes = mutableListOf<Recipe>()
        var completedRecipes = 0

        if (detailedRecipes.isEmpty()) {
            onAllTranslated(emptyList())
            return
        }

        detailedRecipes.forEach { detailed ->
            translater.translate(detailed.title) { translatedTitle ->
                val nutrients = detailed.nutrition.nutrients
                val translatedNutrients = mutableListOf<Nutrient>()
                var completedNutrients = 0

                if (nutrients.isEmpty()) {
                    translatedRecipes.add(
                        Recipe(
                            id = detailed.id,
                            title = translatedTitle ?: detailed.title,
                            image = detailed.image,
                            carbs = null,
                            sugar = null,
                            isSaved = false
                        )
                    )
                    completedRecipes++
                    if (completedRecipes == detailedRecipes.size)
                        onAllTranslated(translatedRecipes)
                    return@translate
                }

                nutrients.forEach { nutrient ->
                    translater.translate(nutrient.name) { translatedNutrientName ->
                        translatedNutrients.add(nutrient.copy(name = translatedNutrientName ?: nutrient.name))
                        completedNutrients++
                        if (completedNutrients == nutrients.size) {
                            val carbs = translatedNutrients
                                .find { it.name.equals("Carbohidratos", ignoreCase = true) || it.name.contains("hidrato", ignoreCase = true) }
                                ?.amount
                            val sugar = translatedNutrients
                                .find { it.name.equals("Azúcar", ignoreCase = true) || it.name.contains("azúcar", ignoreCase = true) }
                                ?.amount
                            translatedRecipes.add(
                                Recipe(
                                    id = detailed.id,
                                    title = translatedTitle ?: detailed.title,
                                    image = detailed.image,
                                    carbs = carbs,
                                    sugar = sugar,
                                    isSaved = false
                                )
                            )
                            completedRecipes++
                            if (completedRecipes == detailedRecipes.size)
                                onAllTranslated(translatedRecipes)
                        }
                    }
                }
            }
        }
    }
}