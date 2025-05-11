package com.martin_dev.sugarit.views.recipie

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.martin_dev.sugarit.backend.viewmodels.RecipieUserViewModel
import com.martin_dev.sugarit.databinding.ActivityUserRecipiesBinding
import com.martin_dev.sugarit.views.recipie.recycler.recipie_saved.RecipieUserAdapter
import kotlinx.coroutines.launch

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
                lifecycleScope.launch {
                    val url = viewModel.fetchRecipeUrlById(recipe.id)
                    if (url != null && (url.startsWith("http://") || url.startsWith("https://"))) {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        if (intent.resolveActivity(packageManager) != null) {
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@UserRecipiesActivity, "No hay navegador disponible", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@UserRecipiesActivity, "No se pudo obtener la URL de la receta", Toast.LENGTH_SHORT).show()
                    }
                }
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
