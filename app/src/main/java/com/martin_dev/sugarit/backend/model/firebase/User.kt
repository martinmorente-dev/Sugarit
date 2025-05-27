package com.martin_dev.sugarit.backend.model.firebase

data class User(
    val id: String,
    val email: String,
    val savedRecipies: Map<Int, Boolean> ?= null,
)