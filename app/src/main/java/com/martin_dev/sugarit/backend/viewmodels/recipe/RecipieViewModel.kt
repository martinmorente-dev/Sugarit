package com.martin_dev.sugarit.backend.viewmodels.recipe

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition.DetailedRecipe
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.nutrition.Nutrient
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Recipe
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp
import kotlinx.coroutines.launch
import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class RecipieViewModel : ViewModel() {

    private val _recipies = MutableLiveData<List<Recipe>>()
    val recipies: LiveData<List<Recipe>> = _recipies

    fun searchByIngredients(ingredients: String) {
        Log.d("RecipieViewModel", "Iniciando búsqueda con ingredientes: $ingredients")
        viewModelScope.launch {
            try {
                val response = Retrofit.api.getRecipeByIngredient(
                    ingredients = ingredients
                )
                Log.d("RecipieViewModel", "Respuesta de findByIngredients: ${response.body()}")
                if (response.isSuccessful) {
                    val recipeResponses = response.body() ?: emptyList()
                    Log.d("RecipieViewModel", "Recipes encontrados: ${recipeResponses.size}")
                    if (recipeResponses.isEmpty()) {
                        _recipies.value = emptyList()
                        Log.d("RecipieViewModel", "No se encontraron recetas")
                        return@launch
                    }

                    val ids = recipeResponses.joinToString(",") { it.id.toString() }
                    Log.d("RecipieViewModel", "IDs para bulk: $ids")

                    val bulkResponse = Retrofit.api.getRecipeBulk(ids = ids, includeNutrition = true)
                    Log.d("RecipieViewModel", "Respuesta de informationBulk: ${bulkResponse.body()}")
                    if (bulkResponse.isSuccessful) {
                        val detailedRecipes = bulkResponse.body() ?: emptyList()
                        Log.d("RecipieViewModel", "DetailedRecipes encontrados: ${detailedRecipes.size}")
                        translateAndMarkRecipes(detailedRecipes)
                    } else {
                        Log.d("RecipieViewModel", "Error en bulk: ${bulkResponse.errorBody()?.string()}")
                        _recipies.value = emptyList()
                    }
                } else {
                    Log.d("RecipieViewModel", "Error en findByIngredients: ${response.errorBody()?.string()}")
                    _recipies.value = emptyList()
                }
            } catch (e: Exception) {
                Log.e("RecipieViewModel", "Excepción: ${e.message}")
                _recipies.value = emptyList()
            }
        }
    }

    private fun translateAndMarkRecipes(
        detailedRecipes: List<DetailedRecipe>
    ) {
        val translater = TranslaterEnToSp()
        val translatedRecipes = mutableListOf<Recipe>()
        var completedRecipes = 0

        Log.d("RecipieViewModel", "Iniciando traducción de recetas: ${detailedRecipes.size}")

        if (detailedRecipes.isEmpty()) {
            _recipies.postValue(emptyList())
            Log.d("RecipieViewModel", "No hay recetas detalladas para traducir")
            return
        }

        detailedRecipes.forEach { detailed ->
            translater.translate(detailed.title) { translatedTitle ->
                val nutrients = detailed.nutrition.nutrients
                val translatedNutrients = mutableListOf<Nutrient>()
                var completedNutrients = 0

                if (nutrients.isEmpty()) {
                    Log.d("RecipieViewModel", "Receta sin nutrientes: ${detailed.title}")
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
                    if (completedRecipes == detailedRecipes.size) {
                        Log.d("RecipieViewModel", "Traducción terminada (sin nutrientes)")
                        markSavedRecipes(translatedRecipes)
                    }
                    return@translate
                }

                nutrients.forEach { nutrient ->
                    translater.translate(nutrient.name) { translatedNutrientName ->
                        translatedNutrients.add(
                            nutrient.copy(name = translatedNutrientName ?: nutrient.name)
                        )
                        completedNutrients++
                        if (completedNutrients == nutrients.size) {
                            val carbs = translatedNutrients
                                .find { it.name.equals("Carbohidratos", ignoreCase = true) || it.name.contains("hidrato", ignoreCase = true) }
                                ?.amount
                            val sugar = translatedNutrients
                                .find { it.name.equals("Azúcar", ignoreCase = true) || it.name.contains("azúcar", ignoreCase = true) }
                                ?.amount

                            Log.d("RecipieViewModel", "Receta traducida: ${translatedTitle ?: detailed.title}, Carbs: $carbs, Sugar: $sugar")

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
                            if (completedRecipes == detailedRecipes.size) {
                                Log.d("RecipieViewModel", "Traducción terminada (con nutrientes)")
                                markSavedRecipes(translatedRecipes)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun markSavedRecipes(recipes: List<Recipe>) {
        Log.d("RecipieViewModel", "Marcando recetas guardadas, total: ${recipes.size}")
        viewModelScope.launch {
            val savedIds = getSavedRecipeIdsFromFirebase()
            Log.d("RecipieViewModel", "IDs guardados en Firebase: $savedIds")
            val recetasMarcadas = recipes.map { receta ->
                receta.copy(isSaved = savedIds.contains(receta.id))
            }
            _recipies.postValue(recetasMarcadas)
        }
    }

    private suspend fun getSavedRecipeIdsFromFirebase(): Set<Int> {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return emptySet()
        val dbRef = FirebaseDatabase.getInstance().getReference("users/$userId/savedRecipies")
        val snapshot = dbRef.get().await()
        val ids = snapshot.children.mapNotNull { it.key?.toIntOrNull() }.toSet()
        Log.d("RecipieViewModel", "IDs obtenidos de Firebase: $ids")
        return ids
    }

    fun saveRecipe(recipeId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").child(recipeId).setValue(true)
        Log.d("RecipieViewModel", "Receta guardada: $recipeId")
    }

    fun deleteRecipie(recipieId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").child(recipieId).removeValue()
        Log.d("RecipieViewModel", "Receta eliminada: $recipieId")
    }

    fun updateRecipieSavedState(recipieId: Int, isSaved: Boolean) {
        _recipies.value = _recipies.value?.map {
            if (it.id == recipieId) it.copy(isSaved = isSaved) else it
        }
        Log.d("RecipieViewModel", "Estado de guardado actualizado para $recipieId: $isSaved")
    }
}
