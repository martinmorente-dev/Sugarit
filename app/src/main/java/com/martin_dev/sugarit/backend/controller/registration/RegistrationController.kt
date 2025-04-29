package com.martin_dev.sugarit.backend.controller.registration

import android.content.Context
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.model.user.User
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.validation.login_password.LoginRegistrationValidation
import com.martin_dev.sugarit.views.components.toast.ToastComponent
import com.martin_dev.sugarit.views.login.LoginActivity
import com.martin_dev.sugarit.views.splashes.email.EmailVerificationActivity

class RegistrationController()
{
    private lateinit var user: User
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    constructor(user: User, context: Context) : this() {
        this.user = user
        this.context = context
    }

    fun registration()
    {
        val validator = LoginRegistrationValidation().validation(this.user.email, this.user.password, this.context)
        auth = FirebaseAuth.getInstance()

        if (validator && !isRegistered(this.user.email.toString(),this.user.password.toString()))
        {
            auth.createUserWithEmailAndPassword(this.user.email.toString(), this.user.password.toString()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val actualUser = auth.currentUser
                        actualUser?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if(verifyTask.isSuccessful)
                            {
                                val intent = Intent(this.context, EmailVerificationActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("email",this.user.email)
                                intent.putExtra("password",this.user.password)
                                intent.putExtra("message_toast","Se te envió un email de confirmación de correo electrónico")
                                this.context.startActivity(intent)
                                auth.signOut()
                            }
                            else
                            {
                                AlertMessage().createAlert("Email not sended", this.context)
                                auth.signOut()
                            }
                        }
                    }
                    else
                        AlertMessage().createAlert("Registration Failed", this.context)
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
