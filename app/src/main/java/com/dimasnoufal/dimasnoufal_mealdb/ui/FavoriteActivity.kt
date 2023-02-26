package com.dimasnoufal.dimasnoufal_mealdb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.dimasnoufal.dimasnoufal_mealdb.adapter.FavoriteAdapter
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ActivityFavoriteBinding
import com.dimasnoufal.dimasnoufal_mealdb.viewmodels.FavoriteViewModel

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private val favoriteViewModel by viewModels<FavoriteViewModel>()
    private val favoriteGameAdapter by lazy { FavoriteAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Favorite Meal")

        favoriteViewModel.favoriteMealList.observe(this) { res ->
            if (res.isEmpty()) {
                binding.apply {
                    rvFavorite.isVisible = false
                    ivEmpty.isVisible = true
                    tvError.isVisible = true
                }
            } else {
                binding.rvFavorite.apply {
                    adapter = favoriteGameAdapter
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(
                        this@FavoriteActivity
                    )
                }

                favoriteGameAdapter.apply {
                    setData(res)
                    setOnItemClickCallback(object:FavoriteAdapter.IOnFavoriteItemCallBack{
                        override fun onFavoriteItemClickCallback(data: MealEntity) {
                            val detailFavorite = Intent(this@FavoriteActivity, FavoriteDetailActivity::class.java)
                            detailFavorite.putExtra(FavoriteDetailActivity.EXTRA_FAVORITE_MEAL,data)
                            startActivity(detailFavorite)
                        }
                    })
                }
            }
        }
    }
}