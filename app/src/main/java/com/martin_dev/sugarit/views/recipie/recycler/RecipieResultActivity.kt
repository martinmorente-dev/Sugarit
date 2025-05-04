package com.martin_dev.sugarit.views.recipie.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.databinding.ActivityRecipieResultBinding

class RecipieResultActivity : AppCompatActivity()
{
    private lateinit var binding: ActivityRecipieResultBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityRecipieResultBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}