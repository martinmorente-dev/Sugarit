package com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Carbs(
    val unit: String,
    val confidenceRange95Percent: ConfidenceRange95Percent
): Parcelable