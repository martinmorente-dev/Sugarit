package com.martin_dev.sugarit.views.menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.card.MaterialCardView
import com.martin_dev.sugarit.R
import com.martin_dev.sugarit.views.food.FoodActivity
import com.martin_dev.sugarit.views.recipie.RecipieActivity

private lateinit var foodCard: MaterialCardView
private lateinit var recipieCard: MaterialCardView

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        init()
        init_listenners()
    }


    private fun init()
    {
        foodCard = findViewById(R.id.food_card)
        recipieCard = findViewById(R.id.recipie_card)
    }


    private fun init_listenners()
    {
        var listenner = View.OnClickListener {view ->
            navigate(view)
        }

        foodCard.setOnClickListener(listenner)
        recipieCard.setOnClickListener(listenner)
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