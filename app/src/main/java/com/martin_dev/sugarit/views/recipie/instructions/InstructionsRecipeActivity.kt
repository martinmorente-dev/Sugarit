package com.martin_dev.sugarit.views.recipie.instructions

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.databinding.ActivityInstructionsRecipeBinding
import com.martin_dev.sugarit.views.recipie.RecipieActivity

class InstructionsRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstructionsRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionsRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        innitListenners()
    }

    private fun innitListenners()
    {
        binding.backBtn.setOnClickListener{
            startActivity(Intent(this, RecipieActivity::class.java))
        }
    }
}