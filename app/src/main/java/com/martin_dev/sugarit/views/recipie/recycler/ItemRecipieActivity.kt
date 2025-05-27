package com.martin_dev.sugarit.views.recipie.recycler

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.databinding.ActivityItemRecipieBinding

class ItemRecipieActivity : AppCompatActivity()
{

    private lateinit var binding : ActivityItemRecipieBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityItemRecipieBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}