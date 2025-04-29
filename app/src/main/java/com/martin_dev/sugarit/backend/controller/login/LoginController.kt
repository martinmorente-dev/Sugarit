package com.martin_dev.sugarit.backend.controller.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.model.user.User
import com.martin_dev.sugarit.backend.validation.login_password.LoginRegistrationValidation
import com.martin_dev.sugarit.views.components.toast.ToastComponent
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.registration.RegistrationActivity

class LoginController()
{

    private lateinit var context: Context
    private lateinit var user: User

    constructor(user: User,context: Context) : this()
    {
        this.user=user
        this.context=context
    }

    fun login()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email,user.password).addOnCompleteListener {
            if(it.isSuccessful)
                ToastComponent().navigationToActivity(MenuActivity::class.java,"Welcome",this.context)
            else
                ToastComponent().navigationToActivity(RegistrationActivity::class.java,"Need registration",this.context)
        }
    }
}