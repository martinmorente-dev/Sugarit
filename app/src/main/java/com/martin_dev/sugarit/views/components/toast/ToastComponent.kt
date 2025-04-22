package com.martin_dev.sugarit.views.components.toast

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat

class ToastComponent
{
    fun navigationToActivity(navigateTo: Class<*>,messageType: String,context: Context)
    {
        val intent = (Intent(context, navigateTo))
        val messageToast: String = setMessageToast(messageType)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("toast_message",messageToast)
        ContextCompat.startActivity(context,intent,null)
    }

    private fun setMessageToast(messageType: String) : String
    {
        var messageToast = ""
        when(messageType)
        {
            "Welcome" -> messageToast = "Bienvenido de nuevo"
            "Need registration" -> messageToast = "Necesitas registrarte para entrar"
            "Is registered" -> messageToast = "¿Olvidaste tu contraseña?"
        }
        return messageToast
    }
}