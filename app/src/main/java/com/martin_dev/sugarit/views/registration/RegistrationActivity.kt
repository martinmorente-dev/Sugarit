package com.martin_dev.sugarit.views.registration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.backend.controller.registration.RegistrationController
import com.martin_dev.sugarit.databinding.ActivityRegistrationBinding
import com.martin_dev.sugarit.views.menu.MenuActivity

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageFromLogin()
        initListenner()
    }

    private fun initListenner()
    {
        binding.btnRegistration.setOnClickListener{
            RegistrationController().registrate()
        }
    }

    private fun messageFromLogin()
    {
        val toastMessage = intent.getStringExtra("toast_message")
        if(!toastMessage.isNullOrBlank())
            Toast.makeText(this,toastMessage,Toast.LENGTH_LONG).show()
    }
}