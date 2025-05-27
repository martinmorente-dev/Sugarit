package com.martin_dev.sugarit.views.recipie

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.martin_dev.sugarit.backend.viewmodels.RecipieUserViewModel
import com.martin_dev.sugarit.databinding.ActivityUserRecipiesBinding
import com.martin_dev.sugarit.views.recipie.recycler.recipie_saved.RecipieUserAdapter

class UserRecipiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRecipiesBinding
    private val viewModel: RecipieUserViewModel by viewModels()
    private lateinit var adapter: RecipieUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupRecyclerView()
        observeRecipes()
        fetchAndLoadUserRecipes()
    }

    private fun setupBinding() {
        binding = ActivityUserRecipiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupRecyclerView() {
        adapter = RecipieUserAdapter(
            onDeleteClickListener = { recipe ->
                viewModel.deleteRecipie(recipe.id.toString())
            },
            onItemClick = { recipe ->
                Log.i("Recipie User","The recipie ${recipe.title}")
            }
        )
        binding.recipieSaveRecycler.layoutManager = LinearLayoutManager(this)
        binding.recipieSaveRecycler.adapter = adapter
    }

    private fun observeRecipes() {
        viewModel.recipiesData.observe(this) { recipes ->
            adapter.setRecipies(recipes)
        }
    }

    private fun fetchAndLoadUserRecipes() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.child("savedRecipies").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipeIds = mutableListOf<String>()
                for (child in snapshot.children) {
                    recipeIds.add(child.key!!)
                }
                if (recipeIds.isNotEmpty()) {
                    viewModel.fetchRecipies(recipeIds)
                }
            }
            override fun onCancelled(error: DatabaseError) {}
        })
    }
}
