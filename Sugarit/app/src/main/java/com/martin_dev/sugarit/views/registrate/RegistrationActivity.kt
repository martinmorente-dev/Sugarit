package com.martin_dev.sugarit.views.registrate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.views.menu.MenuActivity

private lateinit var btnRegistration: MaterialButton

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        init()
        initListenner()
    }

    private fun init()
    {
        btnRegistration = findViewById(R.id.btn_registration)
    }

    private fun initListenner()
    {
        btnRegistration.setOnClickListener{
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}