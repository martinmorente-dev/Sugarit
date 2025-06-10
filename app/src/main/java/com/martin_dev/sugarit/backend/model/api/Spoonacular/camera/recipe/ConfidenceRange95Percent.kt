package com.martin_dev.sugarit.backend.model.api.Spoonacular.camera.recipe

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ConfidenceRange95Percent(
    val max: Double
): Parcelable
