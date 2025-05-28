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
        intent.getStringExtra("urlImage")?: ""
        intent.getStringExtra("recipeName")?: ""
        Log.i("urlImage",intent.getStringExtra("urlImage")?: "No url passed")
        Log.i("recipeName",intent.getStringExtra("recipeName")?: "No name passed")
        binding.title.text = intent.getStringExtra("recipeName")
        Glide.with(this).load(binding.recipeImage.context)
            .load(intent.getStringExtra("urlImage")?.toUri())
            .into(binding.recipeImage)
    }

    private fun innitListenners()
    {
        binding.backBtn.setOnClickListener{
            startActivity(Intent(this, RecipieActivity::class.java))
        }
    }
}