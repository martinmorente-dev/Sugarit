package com.martin_dev.sugarit.backend.controller.Recipe

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage

class RecipieController(private val context: Context, private val lifecycleOwner: LifecycleOwner) {

    fun saveRecipe(recipeId: String)
    {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").child(recipeId).setValue(true)
    }
}