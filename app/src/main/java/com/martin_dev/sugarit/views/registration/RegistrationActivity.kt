package com.martin_dev.sugarit.views.registration

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.backend.controller.registration.RegistrationController
import com.martin_dev.sugarit.backend.model.user.User
import com.martin_dev.sugarit.databinding.ActivityRegistrationBinding
import com.martin_dev.sugarit.views.utils.ToastManager.messageToast

class RegistrationActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        messageToast(this,intent.getStringExtra("toast_message") ?: "")
        initListenner()
    }

    private fun initListenner()
    {
        binding.btnRegistration.setOnClickListener {
            val user = User(binding.username.text.toString(), binding.userPasswd.text.toString())
            RegistrationController(user,this).registration()
        }
    }

}