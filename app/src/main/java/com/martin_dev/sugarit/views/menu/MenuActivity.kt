package com.martin_dev.sugarit.views.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.databinding.ActivityMenuBinding
import com.martin_dev.sugarit.views.food.FoodActivity
import com.martin_dev.sugarit.views.recipie.RecipieActivity

class MenuActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        init_listenners()
    }

    private fun init_listenners()
    {
        var listenner = View.OnClickListener {view ->
            navigate(view)
        }

        binding.foodCard.setOnClickListener(listenner)
        binding.recipieCard.setOnClickListener(listenner)
    }

    private fun navigate(view: View)
    {
        when(view.id)
        {
            R.id.food_card -> startActivity(Intent(this, FoodActivity::class.java))
            R.id.recipie_card -> startActivity(Intent(this, RecipieActivity::class.java))
        }
    }
}