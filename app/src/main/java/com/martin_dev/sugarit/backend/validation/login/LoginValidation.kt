package com.martin_dev.sugarit.backend.validation.login

import android.content.Context
import android.util.Patterns
import com.martin_dev.sugarit.backend.validation.AlertMessage

class LoginValidation
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