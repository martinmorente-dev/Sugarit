package com.martin_dev.sugarit.backend.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.BuildConfig
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Nutrient
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.RecipieResponse
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import kotlinx.coroutines.launch
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class RecipieViewModel : ViewModel() {

    private val _recipies = MutableLiveData<List<Recipe>>()
    val recipies: LiveData<List<Recipe>> = _recipies

    fun searchByIngredients(ingredients: String, alergies: String) {
        viewModelScope.launch {
            try {
                val response = Retrofit.api.getRecipieByIngredient(
                    ingredients,
                    apiKey = BuildConfig.API_KEY,
                    intolerances = alergies
                )
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        translatedList(listOf(body)) { onAllTranslated ->
                            val traducedRecipies = onAllTranslated.flatMap { it.results }
                            viewModelScope.launch {
                                val savedIds = getSavedRecipeIdsFromFirebase()
                                val recetasMarcadas = traducedRecipies.map { receta ->
                                    receta.copy(isSaved = savedIds.contains(receta.id))
                                }
                                _recipies.postValue(recetasMarcadas)
                            }
                        }
                    } else {
                        _recipies.value = emptyList()
                    }
                } else {
                    _recipies.value = emptyList()
                }
            } catch (e: Exception) {
                _recipies.value = emptyList()
                Log.e("RecipieViewModel", "Error: ${e.message}")
            }
        }
    }

    fun translatedList(apiResults: List<RecipieResponse>, onAllTranslated: (List<RecipieResponse>) -> Unit) {
        val translater = TranslaterEnToSp()
        val translatedRecipes = mutableListOf<Recipe>()
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

    private suspend fun getSavedRecipeIdsFromFirebase(): Set<Int> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return emptySet()
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/savedRecipies")
        val snapshot = dbRef.get().await()
        return snapshot.children.mapNotNull { it.key?.toIntOrNull() }.toSet()
    }

    fun saveRecipe(recipeId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").child(recipeId).setValue(true)
    }

    fun deleteRecipie(recipieId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").child(recipieId).removeValue()
    }

    fun updateRecipieSavedState(recipieId: Int, isSaved: Boolean) {
        _recipies.value = _recipies.value?.map {
            if (it.id == recipieId) it.copy(isSaved = isSaved) else it
        }
    }
}
