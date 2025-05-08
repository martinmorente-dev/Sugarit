package com.martin_dev.sugarit.backend.model.api.Spoonacular.recipies

import com.google.gson.annotations.SerializedName

data class RecipieUrl(
    @SerializedName("sourceUrl")
    val url: String?
)
