package com.martin_dev.sugarit.backend.utilites.retrofit

import com.martin_dev.sugarit.backend.controller.api.spoonacular.SpoonAPIService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Retrofit
{
    private const val BASE_URL = "https://api.spoonacular.com"

    val api: SpoonAPIService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SpoonAPIService::class.java)
    }
}