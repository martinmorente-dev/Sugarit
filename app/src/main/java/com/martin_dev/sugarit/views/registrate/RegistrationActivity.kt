package com.martin_dev.sugarit.views.registrate

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.databinding.ActivityRegistrationBinding
import com.martin_dev.sugarit.views.menu.MenuActivity

class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListenner()
    }


    private fun initListenner()
    {
        binding.btnRegistration.setOnClickListener{
            startActivity(Intent(this, MenuActivity::class.java))
        }
    }
}