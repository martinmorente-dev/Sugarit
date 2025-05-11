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
import com.martin_dev.sugarit.backend.viewmodels.RecipieURLViewModel

class RecipieController(private val context: Context, private val lifecycleOwner: LifecycleOwner) {

    private val urlViewModel: RecipieURLViewModel by lazy {
        ViewModelProvider(context as FragmentActivity)[RecipieURLViewModel::class.java]
    }

    fun searchRecipieUrl(recipeId: Int)
    {
        urlViewModel.searchRecipieUrl(recipeId)
        urlViewModel.recipeurl.observe(lifecycleOwner) { recipieResponse ->
            val url = recipieResponse.url
            if(!url.isNullOrBlank())
            {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            }
            else
                AlertMessage().createAlert("No url found", context)
        }
    }

    fun saveRecipe(recipeId: String)
    {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").child(recipeId).setValue(true)
    }
}