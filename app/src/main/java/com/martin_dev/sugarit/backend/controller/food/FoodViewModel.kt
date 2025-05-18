package com.martin_dev.sugarit.backend.controller.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Food
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Nutrition
import kotlinx.coroutines.launch

class FoodViewModel : ViewModel() {
    private val _food = MutableLiveData<Food>()
    val food: LiveData<Food?> = _food

    private val _nutrition = MutableLiveData<Nutrition>()
    val nutrition: LiveData<Nutrition?> = _nutrition

    fun searchFood(query: String) {
        viewModelScope.launch {
            _food.postValue(FoodRepositorySingleton.repository.searchFood(query))
        }
    }

    fun fetchFoodNutrition(foodId: Int, amount: Int) {
        viewModelScope.launch {
            _nutrition.postValue(FoodRepositorySingleton.repository.getFoodNutrition(foodId, amount))
        }
    }
}
