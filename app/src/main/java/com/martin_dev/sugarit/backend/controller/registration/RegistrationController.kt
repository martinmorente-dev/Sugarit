package com.martin_dev.sugarit.backend.controller.registration

import android.content.Context
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.validation.login_password.LoginRegistrationValidation
import com.martin_dev.sugarit.views.components.toast.ToastComponent
import com.martin_dev.sugarit.views.login.LoginActivity
import com.martin_dev.sugarit.views.menu.MenuActivity

class RegistrationController() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var context: Context

    constructor(email: String, password: String, context: Context) : this() {
        this.email = email
        this.password = password
        this.context = context
    }

    fun registration()
    {
        val validator = LoginRegistrationValidation().validation(this.email, this.password, this.context)


        if (validator && !isRegistered(this.email,this.password))
        {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(this.email.toString(), this.password.toString()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        Log.i("Succesful","Creacion")
                        ToastComponent().navigationToActivity(MenuActivity::class.java,"Welcome",this.context)
                    }
                    else
                    {
                        Log.i("Failed","Fallo la creacion")
                        AlertMessage().createAlert("Registration Failed", this.context)
                    }
            }
        }
        else
            AlertMessage().createAlert("This user exist", this.context)
    }

    fun isRegistered(email: String, password: String): Boolean
    {
        var comprobation: Boolean = false
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if(task.isSuccessful)
                {
                    ToastComponent().navigationToActivity(LoginActivity::class.java, "Is registered", this.context)
                    comprobation = true
                }
            }
        return comprobation
    }
}
