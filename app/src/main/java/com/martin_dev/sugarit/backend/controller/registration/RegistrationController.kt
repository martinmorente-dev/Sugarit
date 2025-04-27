package com.martin_dev.sugarit.backend.controller.registration

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.backend.validation.login_password.LoginRegistrationValidation
import com.martin_dev.sugarit.views.components.toast.ToastComponent
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.login.LoginActivity
import com.martin_dev.sugarit.views.registration.RegistrationActivity
import com.martin_dev.sugarit.views.splashes.email.EmailVerificationActivity

class RegistrationController() {
    private lateinit var email: String
    private lateinit var password: String
    private lateinit var context: Context
    private lateinit var auth: FirebaseAuth

    constructor(email: String, password: String, context: Context) : this() {
        this.email = email
        this.password = password
        this.context = context
    }

    fun registration()
    {
        val validator = LoginRegistrationValidation().validation(this.email, this.password, this.context)
        auth = FirebaseAuth.getInstance()

        if (validator && !isRegistered(this.email,this.password))
        {
            auth.createUserWithEmailAndPassword(this.email.toString(), this.password.toString()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val user = auth.currentUser
                        user?.sendEmailVerification()?.addOnCompleteListener { verifyTask ->
                            if(verifyTask.isSuccessful)
                            {
                                val intent = Intent(this.context, EmailVerificationActivity::class.java)
                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                intent.putExtra("email",this.email)
                                intent.putExtra("password",this.password)
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
