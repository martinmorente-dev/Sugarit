package com.martin_dev.sugarit.backend.validation.login_password

import android.content.Context
import android.util.Patterns
import com.martin_dev.sugarit.backend.validation.AlertMessage

class LoginRegistrationValidation
{
    fun validation(email: String, password: String, context: Context): Boolean
    {
        val alert = AlertMessage()
        val specialCharacters = setOf('*', '-', '/', ':', '!', '@', '#', '$', '%', '&', '¡', '¿', '?')
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
            password.length < 6 -> {
                alert.createAlert("Malformed password Length",context)
                return false
            }
            !password.any {it.isUpperCase()} -> {
                alert.createAlert("Malformed password Upper",context)
                return false
            }
            !password.any {it.isLowerCase()} -> {
                alert.createAlert("Malformed password Lower",context)
                return false
            }
            !password.any{it.isDigit()} -> {
                alert.createAlert("Malformed password Digit",context)
                return false
            }
            !password.any{ it in specialCharacters } -> {
                alert.createAlert("Malformed password Special Char",context)
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