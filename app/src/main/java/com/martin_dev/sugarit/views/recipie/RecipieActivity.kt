package com.martin_dev.sugarit.views.recipie

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.controller.recipie.RecipieController
import com.martin_dev.sugarit.databinding.ActivityRecipieBinding

class RecipieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    fun initListeners()
    {
        binding.sendBtn.setOnClickListener {
            RecipieController().serchByIngredients(binding.userIngredients.editText?.text.toString())
        }
    }
}