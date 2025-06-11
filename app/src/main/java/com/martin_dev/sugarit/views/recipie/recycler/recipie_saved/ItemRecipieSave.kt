package com.martin_dev.sugarit.views.recipie.recycler.recipie_saved

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.martin_dev.sugarit.databinding.ActivityItemRecipieSaveBinding

class ItemRecipieSave : AppCompatActivity() {

    private lateinit var binding: ActivityItemRecipieSaveBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityItemRecipieSaveBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }


}