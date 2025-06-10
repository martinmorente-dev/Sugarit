package com.martin_dev.sugarit.backend.controller.login

import android.content.Context
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.martin_dev.sugarit.backend.utilites.user.UserBasics
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
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
        if(userBasics.email.isEmpty() || userBasics.password.isEmpty())
            AlertMessage().createAlert("Empty email or password",this.context)
        else
        {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(userBasics.email, userBasics.password)
                .addOnCompleteListener {
                    if (it.isSuccessful)
                        ToastComponent().navigationToActivity(
                            MenuActivity::class.java,
                            "Welcome",
                            this.context
                        )
                    else
                    {
                        val exception = it.exception
                        when (exception) {
                            is FirebaseAuthInvalidCredentialsException -> AlertMessage().createAlert("Wrong access", this.context)
                            is FirebaseNetworkException -> AlertMessage().createAlert("Network error", this.context)
                            is FirebaseTooManyRequestsException -> AlertMessage().createAlert("Too many requests", this.context)
                            else -> AlertMessage().createAlert("Unknown error", this.context)
                        }
                    }
                }
        }
    }
}