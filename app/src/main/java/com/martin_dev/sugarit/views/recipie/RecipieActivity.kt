package com.martin_dev.sugarit.views.recipie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.controller.recipie.RecipieController
import com.martin_dev.sugarit.backend.validation.AlertMessage
import com.martin_dev.sugarit.databinding.ActivityRecipieBinding
import com.martin_dev.sugarit.views.recipie.recycler.RecipieResultActivity

class RecipieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipieBinding
    private lateinit var alergies: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initListeners()
    }

    fun initListeners()
    {
        binding.sendBtn.setOnClickListener {
            AlertMessage().createAlert("alergiesInfo",this) {inputUser ->
                alergies = inputUser
                RecipieController().serchByIngredients(binding.userIngredients.editText?.text.toString(),alergies)
                startActivity(Intent(this, RecipieResultActivity::class.java))
            }
        }
    }
}