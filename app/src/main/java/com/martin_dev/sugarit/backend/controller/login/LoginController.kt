package com.martin_dev.sugarit.backend.controller.login

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.utilites.user.UserBasics
import com.martin_dev.sugarit.views.components.toast.ToastComponent
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.registration.RegistrationActivity

class LoginController()
{

    private lateinit var context: Context
    private lateinit var userBasics: UserBasics

    constructor(userBasics: UserBasics, context: Context) : this()
    {
        this.userBasics=userBasics
        this.context=context
    }

    fun login()
    {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userBasics.email,userBasics.password).addOnCompleteListener {
            if(it.isSuccessful)
                ToastComponent().navigationToActivity(MenuActivity::class.java,"Welcome",this.context)
            else
                ToastComponent().navigationToActivity(RegistrationActivity::class.java,"Need registration",this.context)
        }
    }
}