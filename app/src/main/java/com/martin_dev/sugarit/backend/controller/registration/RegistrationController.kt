package com.martin_dev.sugarit.backend.controller.registration

import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.martin_dev.sugarit.backend.utilites.user.UserBasics
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.views.components.toast.ToastComponent
import com.martin_dev.sugarit.views.login.LoginActivity
import com.martin_dev.sugarit.views.splashes.email.EmailVerificationActivity

class RegistrationController()
{
    private lateinit var userBasics: UserBasics
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    constructor(userBasics: UserBasics, context: Context) : this() {
        this.userBasics = userBasics
        this.context = context
    }

    fun registration()
    {
        auth = FirebaseAuth.getInstance()
        if(userBasics.email.isEmpty() || userBasics.password.isEmpty())
        {
            AlertMessage().createAlert("Empty email or password", this.context)
            return
        }
        if (!isRegistered(this.userBasics.email.toString(),this.userBasics.password.toString()))
        {
            auth.createUserWithEmailAndPassword(this.userBasics.email.toString(), this.userBasics.password.toString()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val actualUser = auth.currentUser
                        actualUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if(verifyTask.isSuccessful)
                            {
                                val intent = Intent(this.context, EmailVerificationActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("email",this.userBasics.email)
                                intent.putExtra("password",this.userBasics.password)
                                intent.putExtra("message_toast","Se te envió un email de confirmación de correo electrónico")
                                this.context.startActivity(intent)
                                auth.signOut()
                            }
                            else
                            {
                                AlertMessage().createAlert("Email not sended", this.context)
                                FirebaseAuth.getInstance().currentUser?.delete()
                            }
                        }
                    }
                    else
                    {
                        val exception = it.exception
                        when(exception)
                        {
                            is FirebaseAuthInvalidUserException -> AlertMessage().createAlert("Invalid email", this.context)
                            is FirebaseAuthWeakPasswordException -> AlertMessage().createAlert("Weak password", this.context)
                            is FirebaseAuthInvalidCredentialsException -> AlertMessage().createAlert("Invalid email", this.context)
                            else ->
                            {
                                val message = exception?.message ?: ""
                                if(message.contains("PASSWORD_DOES_NOT_MEET_REQUIREMENTS"))
                                    AlertMessage().createAlert("Weak password", this.context)
                                else
                                {
                                    AlertMessage().createAlert("Unknown error", this.context)
                                    return@addOnCompleteListener
                                }
                            }
                        }
                    }
            }
        }
        else
            AlertMessage().createAlert("This user exist", this.context)
    }

    private fun isRegistered(email: String, password: String): Boolean
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
