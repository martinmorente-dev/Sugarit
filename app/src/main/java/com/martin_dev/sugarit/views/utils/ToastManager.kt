package com.martin_dev.sugarit.views.utils

import android.content.Context
import android.widget.Toast

object ToastManager
{
    fun messageToast(context: Context,message: String)
    {
        if(!message.isNullOrBlank())
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }
}