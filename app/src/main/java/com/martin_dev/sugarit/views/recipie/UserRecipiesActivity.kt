package com.martin_dev.sugarit.views.recipie

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.martin_dev.sugarit.backend.viewmodels.recipe.RecipieUserViewModel
import com.martin_dev.sugarit.backend.viewmodels.recipe.RecipieViewModel
import com.martin_dev.sugarit.databinding.ActivityUserRecipiesBinding
import com.martin_dev.sugarit.views.recipie.recycler.recipie_saved.RecipieUserAdapter

class UserRecipiesActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserRecipiesBinding
    private val viewModel: RecipieUserViewModel by viewModels()
    private val viewModelRecipieResult: RecipieViewModel by viewModels()
    private lateinit var adapter: RecipieUserAdapter

    private var favoritesListener: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupBinding()
        setupRecyclerView()
        observeRecipes()
        fetchAndLoadUserRecipesRealtime()
    }

    private fun setupBinding() {
        binding = ActivityUserRecipiesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun setupRecyclerView() {
        adapter = RecipieUserAdapter(
            onDeleteClickListener = { recipe ->
                viewModelRecipieResult.deleteRecipie(recipe.id.toString())
                viewModelRecipieResult.updateRecipieSavedState(recipe.id, false)
                Toast.makeText(this, "Receta ${recipe.title} eliminada", Toast.LENGTH_SHORT).show()
            },
            onItemClick = { recipe ->
                Log.i("Recipie User", "The recipie ${recipe.title}")
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

    private fun fetchAndLoadUserRecipesRealtime() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        favoritesListener?.let { userRef.child("savedRecipies").removeEventListener(it) }
        favoritesListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val recipeIds = mutableListOf<String>()
                for (child in snapshot.children) {
                    recipeIds.add(child.key!!)
                }
                viewModel.fetchRecipies(recipeIds)
            }
            override fun onCancelled(error: DatabaseError) {}
        }
        userRef.child("savedRecipies").addValueEventListener(favoritesListener!!)
    }

    override fun onDestroy() {
        super.onDestroy()
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        favoritesListener?.let { userRef.child("savedRecipies").removeEventListener(it) }
    }
}
