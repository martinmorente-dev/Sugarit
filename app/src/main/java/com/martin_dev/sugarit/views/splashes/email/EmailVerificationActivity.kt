package com.martin_dev.sugarit.views.splashes.email

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.databinding.ActivityEmailVerificationBinding
import com.martin_dev.sugarit.views.login.LoginActivity
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.utils.ToastManager.messageToast
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class EmailVerificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailVerificationBinding
    private var auth: FirebaseAuth = FirebaseAuth.getInstance()
    private lateinit var email: String
    private lateinit var password: String

    private val VERIFICATION_TIMEOUT = 86400000L // 24 hours in milliseconds
    private val CHECK_INTERVAL = 3000L // 3 seconds interval for checking email verification

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.resendEmailBtn.visibility = View.GONE

        email = intent.getStringExtra("email").toString()
        password = intent.getStringExtra("password").toString()
        val message = intent.getStringExtra("message_toast").toString()
        messageToast(this, message)

        checkEmail(email, password)
        initListeners()
    }

    private fun checkEmail(email: String, password: String) {
        lifecycleScope.launch {
            val startTime = System.currentTimeMillis()

            while (System.currentTimeMillis() - startTime < VERIFICATION_TIMEOUT)
            {
                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful)
                    {
                        if (auth.currentUser?.isEmailVerified == true)
                        {
                            startActivity(Intent(this@EmailVerificationActivity, MenuActivity::class.java))
                            finish()
                            return@addOnCompleteListener
                        }
                    }
                }
                delay(CHECK_INTERVAL)
            }
            deleteUserAccount()
        }
    }

    private fun deleteUserAccount()
    {
        val currentUser = auth.currentUser
        currentUser?.delete()?.addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                Log.d("EmailVerification", "User account deleted due to unverified email.")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            else
                Log.e("EmailVerification", "Failed to delete user account.")
        }
    }

    private fun initListeners()
    {
        binding.resendEmailBtn.setOnClickListener {
            resendEmail()
        }
    }

    private fun resendEmail() {
        auth.currentUser?.sendEmailVerification()?.addOnCompleteListener { task ->
            if (task.isSuccessful)
            {
                binding.txtStatus.text = "Correo reenviado. ¡Revisa tu bandeja de entrada!"
                binding.resendEmailBtn.isEnabled = false
                lifecycleScope.launch {
                    delay(60000L)
                    binding.resendEmailBtn.isEnabled = true
                }
            }
            else
                AlertMessage().createAlert("Email not sent", this)
        }
    }
}
