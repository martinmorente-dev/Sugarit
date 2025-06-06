package com.martin_dev.sugarit.views.recipie

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterSpToEn
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.databinding.ActivityRecipieBinding
import com.martin_dev.sugarit.views.menu.MenuActivity
import com.martin_dev.sugarit.views.recipie.recycler.RecipieResultActivity

class RecipieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipieBinding
    private val selectedIngredients = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@RecipieActivity, MenuActivity::class.java))
            } })
        initListeners()
    }

    private fun initListeners() {
        binding.sendBtn.setOnClickListener {
                val ingredients = binding.ingredients.text.toString()
                val intent = Intent(this, RecipieResultActivity::class.java)
                TranslaterSpToEn().translate(ingredients) { translatedText ->
                    intent.putExtra("ingredients", translatedText.toString())
                    startActivity(intent)
                }
        }

    }
}