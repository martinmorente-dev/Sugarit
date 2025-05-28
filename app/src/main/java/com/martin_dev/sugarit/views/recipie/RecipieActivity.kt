package com.martin_dev.sugarit.views.recipie

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterSpToEn
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.databinding.ActivityRecipieBinding
import com.martin_dev.sugarit.views.recipie.recycler.RecipieResultActivity

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
            AlertMessage().createAlert("alergiesInfo",this) {inputUser ->
                val alergies: String = inputUser
                var ingredients: String = binding.userIngredients.editText?.text.toString()
                val intent = Intent(this, RecipieResultActivity::class.java)
                TranslaterSpToEn().translate(ingredients) { translatedText ->
                    ingredients = translatedText.toString()
                    intent.putExtra("ingredients", ingredients)
                    intent.putExtra("alergies",alergies)
                    startActivity(intent)
                }
            }
        }
    }
}