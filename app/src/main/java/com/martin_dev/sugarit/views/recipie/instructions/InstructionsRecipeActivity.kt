package com.martin_dev.sugarit.views.recipie.instructions

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.martin_dev.sugarit.databinding.ActivityInstructionsRecipeBinding
import com.martin_dev.sugarit.views.recipie.RecipieActivity

class InstructionsRecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInstructionsRecipeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInstructionsRecipeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        innitListenners()
        binding.title.text = intent.getStringExtra("recipeName")
        Glide.with(this).load(binding.recipeImage.context)
            .load(intent.getStringExtra("urlImage")?.toUri())
            .into(binding.recipeImage)
    }

    private fun innitListenners()
    {
        binding.backBtn.bringToFront()
        binding.backBtn.setOnClickListener{
            startActivity(Intent(this, RecipieActivity::class.java))
        }
    }
}