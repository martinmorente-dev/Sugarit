package com.martin_dev.sugarit.backend.controller.registration

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.validation.login.LoginValidation
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.registration.RegistrationActivity

class RegistrationController()
{
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var context: Context

    constructor(email: String, password: String, context: Context) : this() {
        this.email = email
        this.password = password
        this.context=context
    }

    fun registrate()
    {
        val validator = LoginValidation().validation(this.email,this.password,this.context)

        if(validator)
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(this.email,this.password).addOnCompleteListener {
                when
                {
                    it.isSuccessful -> navigationToActivity(MenuActivity::class.java)
                    else -> {
                        AlertMessage().createAlert("Registration Failed",this.context)
                    }
                }
            }
        }
    }

    private fun navigationToActivity(navigateTo: Class<*>)
    {
        val intent = (Intent(this.context, navigateTo))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("toast_message","Necesitas registrarte")
        ContextCompat.startActivity(this.context,intent,null)
    }

}