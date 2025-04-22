package com.martin_dev.sugarit.backend.controller.login

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.validation.login.LoginValidation
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.registration.RegistrationActivity

class LoginController()
{
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var context: Context

    constructor(email: String, password: String, context: Context) : this() {
        this.email = email
        this.password = password
        this.context=context
    }

    fun login()
    {
        val validator = LoginValidation().validation(this.email,this.password,this.context)

        if(validator)
        {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(this.email,this.password).addOnCompleteListener {
                if(it.isSuccessful)
                   navigationToActivity(MenuActivity::class.java,"Welcome")
                else
                    navigationToActivity(RegistrationActivity::class.java,"Need registration")
            }
        }
    }

    private fun navigationToActivity(navigateTo: Class<*>,messageType: String)
    {
        val intent = (Intent(this.context, navigateTo))
        val messageToast: String = setMessageToast(messageType)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("toast_message",messageToast)
        ContextCompat.startActivity(this.context,intent,null)
    }

    private fun setMessageToast(messageType: String) : String
    {
        var messageToast = ""
        when(messageType)
        {
            "Welcome" -> messageToast = "Bienvenido de nuevo"
            "Need registration" -> messageToast = "Necesitas registrarte para entrar"
        }
        return messageToast
    }

}