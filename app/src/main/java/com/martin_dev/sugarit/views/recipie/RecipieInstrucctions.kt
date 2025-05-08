package com.martin_dev.sugarit.views.recipie

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.viewmodels.RecipieURLViewModel

class RecipieInstrucctions(private val context: Context, private val lifecycleOwner: LifecycleOwner) {

    private val urlViewModel: RecipieURLViewModel by lazy {
        ViewModelProvider(context as androidx.fragment.app.FragmentActivity)[RecipieURLViewModel::class.java]
    }

    fun searchRecipieUrl(recipeId: Int)
    {
        urlViewModel.searchRecipieUrl(recipeId)
        urlViewModel.recipeurl.observe(lifecycleOwner) { recipieResponse ->
            val url = recipieResponse.url
            Log.d("WebViewURL", "Cargando URL: $url")
            if(!url.isNullOrBlank())
            {
                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                context.startActivity(browserIntent)
            }
            else
                AlertMessage().createAlert("No url found", context)
        }
    }
}
