package com.martin_dev.sugarit.backend.validation.register

import android.content.Context
import android.util.Patterns
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.controller.registration.RegistrationController
import com.martin_dev.sugarit.backend.validation.AlertMessage

class RegistrationValidation
{
    fun validation(email: String, password: String, context: Context): Boolean
    {
        val alert = AlertMessage()
        when
        {
            email.isEmpty() || password.isEmpty() -> {
                alert.createAlert("Empty email or password",context)
                return false
            }
            !isEmailValid(email) -> {
                alert.createAlert("Wrong email",context)
                return false
            }

        }
        return true
    }

    fun isEmailValid(email: String): Boolean
    {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}