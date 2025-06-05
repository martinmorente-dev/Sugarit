package com.martin_dev.sugarit.backend.controller.food

import com.martin_dev.sugarit.backend.utilites.retrofit.Retrofit

object FoodRepositorySingleton {
    val repositoryFood: FoodRepository by lazy { FoodRepository(Retrofit.api) }


}
