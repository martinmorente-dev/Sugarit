package com.martin_dev.sugarit.backend.controller.food

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.martin_dev.sugarit.backend.model.api.Spoonacular.food.Food
import com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies.Nutrition
import kotlinx.coroutines.launch

class FoodViewModel(): ViewModel()
{
    private val _food = MutableLiveData<Food>()
    val food: LiveData<Food?> = _food
    lateinit var foodRepository: FoodRepository

    private val _nutrition = MutableLiveData<Nutrition>()
    val nutrition: LiveData<Nutrition?> = _nutrition

    fun searchFood(query: String)
    {
        viewModelScope.launch {
            _food.postValue(foodRepository.searchFood(query))
        }
    }

    fun fetchFoodNutrition(foodId: Int, amount: Int)
    {
        viewModelScope.launch {
            _nutrition.postValue(foodRepository.getFoodNutrition(foodId, amount))
        }
    }

}