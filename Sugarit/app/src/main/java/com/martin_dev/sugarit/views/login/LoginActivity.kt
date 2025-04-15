package com.martin_dev.sugarit.views.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.registrate.RegistrationActivity

private lateinit var btnLogin: MaterialButton
private lateinit var btnRegistration: MaterialButton

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        init_listenners()
    }

    private fun init()
    {
        btnLogin = findViewById(R.id.btn_login)
        btnRegistration = findViewById(R.id.btn_registration)
    }

    private fun init_listenners()
    {
        val listenner = View.OnClickListener { view ->
            navigation(view)
        }
        btnLogin.setOnClickListener(listenner)
        btnRegistration.setOnClickListener(listenner)
    }

    private fun navigation(view: View)
    {
        when(view.id)
        {
            R.id.btn_login -> startActivity(Intent(this, MenuActivity::class.java))
            R.id.btn_registration -> startActivity(Intent(this, RegistrationActivity::class.java))
        }
    }

}