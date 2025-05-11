package com.martin_dev.sugarit.views.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.backend.controller.login.LoginController
import com.martin_dev.sugarit.backend.utilites.user.UserBasics
import com.martin_dev.sugarit.databinding.ActivityLoginBinding
import com.martin_dev.sugarit.views.registration.RegistrationActivity
import com.martin_dev.sugarit.views.utils.ToastManager.messageToast

class LoginActivity : AppCompatActivity() {
    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageToast(this,intent.getStringExtra("toast_message") ?: "")
        init_listenners()
    }

    private fun init_listenners()
    {
        val listenner = View.OnClickListener { view ->
            navigation(view)
        }
        binding.btnLogin.setOnClickListener(listenner)
        binding.btnRegistration.setOnClickListener(listenner)
    }

    private fun navigation(view: View)
    {
        val userBasics = UserBasics(binding.username.text.toString(), binding.userPasswd.text.toString())
        when(view.id)
        {
            R.id.btn_login -> LoginController(userBasics,this).login()
            R.id.btn_registration -> startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }
}