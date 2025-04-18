package com.martin_dev.sugarit.backend.controller.login

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.martin_dev.sugarit.views.login.LoginActivity

public class LoginController()
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

    }

    private fun validation(): Boolean
    {
        if (email.isEmpty())
        {

            return false
        }
        return true
    }

    private fun showAlert()
    {
        val buider = AlertDialog.Builder(this.context)
        buider.setTitle("Error")
        // Message Logic
    }
}