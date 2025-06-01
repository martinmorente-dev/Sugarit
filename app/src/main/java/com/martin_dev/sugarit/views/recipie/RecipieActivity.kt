package com.martin_dev.sugarit.views.recipie

import IngredientAutoCompleteViewModel
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterSpToEn
import com.martin_dev.sugarit.backend.utilites.validation.AlertMessage
import com.martin_dev.sugarit.databinding.ActivityRecipieBinding
import com.martin_dev.sugarit.views.recipie.recycler.RecipieResultActivity

class RecipieActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRecipieBinding
    private val autocompleteViewModel: IngredientAutoCompleteViewModel by viewModels()
    private val selectedIngredients = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.clearSelectedBtn.visibility = View.GONE
        setupAutocomplete()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun setupAutocomplete() {
        val autoComplete = binding.autoCompleteIngredients

        autoComplete.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                val query = s.toString()
                if (query.length >= 2) {
                    autocompleteViewModel.fetchSuggestions(query)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        autocompleteViewModel.suggestions.observe(this) { suggestions ->
            val adapter = ArrayAdapter(
                this,
                android.R.layout.simple_dropdown_item_1line,
                suggestions.map { it.name })
            autoComplete.setAdapter(adapter)
            adapter.notifyDataSetChanged()
        }

        autoComplete.setOnItemClickListener { parent, _, position, _ ->
            binding.clearSelectedBtn.visibility = View.VISIBLE
            val selected = parent.getItemAtPosition(position) as String
            if (!selectedIngredients.contains(selected)) {
                selectedIngredients.add(selected)
            }
            autoComplete.text.clear()
            binding.selectedIngredientsText.text = selectedIngredients.joinToString(", ")
        }
    }

    @SuppressLint("SetTextI18n")
    private fun initListeners() {
        binding.sendBtn.setOnClickListener {
            AlertMessage().createAlert("alergiesInfo", this) { inputUser ->
                val alergies: String = inputUser
                val ingredients = selectedIngredients.joinToString(",")
                val intent = Intent(this, RecipieResultActivity::class.java)
                TranslaterSpToEn().translate(ingredients) { translatedText ->
                    intent.putExtra("ingredients", translatedText.toString())
                    intent.putExtra("alergies", alergies)
                    startActivity(intent)
                }
            }

        }
        binding.clearSelectedBtn.setOnClickListener {
            selectedIngredients.clear()
            binding.selectedIngredientsText.text = "Seleccionados: "
        }
    }
}