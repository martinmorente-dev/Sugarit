package com.martin_dev.sugarit.views.recipie.recycler

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.databinding.ActivityRecipeResultErrorBinding
import com.martin_dev.sugarit.views.recipie.RecipieActivity

class RecipeResultErrorActivity : AppCompatActivity()
{

    private lateinit var binding: ActivityRecipeResultErrorBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipeResultErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@RecipeResultErrorActivity, RecipieActivity::class.java))

            } })
        innitListener()
    }

    private fun innitListener()
    {
        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, RecipieActivity::class.java))
        }
    }


}