package com.martin_dev.sugarit.backend.viewmodels.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.controller.food.FoodRepositorySingleton
import com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.ingredient.Food
import com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.ingredient.Nutrition
import com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.recipe.Carbs
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    private val _food = MutableLiveData<Food>()
    val food: LiveData<Food?> = _food

    private val _nutrition = MutableLiveData<Nutrition>()
    val nutrition: LiveData<Nutrition?> = _nutrition

    private val _recipeName = MutableLiveData<Carbs>()
    val recipeName: LiveData<Carbs?> = _recipeName

    fun searchFood(query: String) {
        viewModelScope.launch {
            _food.postValue(FoodRepositorySingleton.repositoryFood.searchFood(query))
        }
    }

    fun fetchFoodNutrition(foodId: Int, amount: Int) {
        viewModelScope.launch {
            _nutrition.postValue(FoodRepositorySingleton.repositoryFood.getFoodNutrition(foodId, amount))
        }
    }


    fun fetchRecipeNutrition(name: String) {
        viewModelScope.launch {
            _recipeName.postValue(FoodRepositorySingleton.repositoryFood.getRecipeName(name))
        }
    }

}