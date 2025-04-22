package com.martin_dev.sugarit.backend.controller.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.validation.login_password.LoginRegistrationValidation
import com.martin_dev.sugarit.views.components.toast.ToastComponent
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
        val validator = LoginRegistrationValidation().validation(this.email,this.password,this.context)

        if(validator)
        {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(this.email,this.password).addOnCompleteListener {
                if(it.isSuccessful)
                   ToastComponent().navigationToActivity(MenuActivity::class.java,"Welcome",this.context)
                else
                    ToastComponent().navigationToActivity(RegistrationActivity::class.java,"Need registration",this.context)
            }
        }
    }
}