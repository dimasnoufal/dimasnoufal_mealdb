package com.dimasnoufal.dimasnoufal_mealdb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dimasnoufal.dimasnoufal_mealdb.R
import com.dimasnoufal.dimasnoufal_mealdb.adapter.RecommendationAdapter
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ActivityFavoriteDetailBinding
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemList
import com.dimasnoufal.dimasnoufal_mealdb.viewmodels.FavoriteDetailViewModel

class FavoriteDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteDetailBinding
    private val favoriteDetailViewModel by viewModels<FavoriteDetailViewModel>()
    private val recommendationAdapter by lazy { RecommendationAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Favorite Detail Meal")

        var favoriteMeal = intent.getParcelableExtra<MealEntity>(EXTRA_FAVORITE_MEAL)
        binding.mealFavoriteDetail = favoriteMeal!!.meal
        binding.apply {
            Glide.with(this@FavoriteDetailActivity)
                .load(favoriteMeal.meal?.strMealThumb)
                .error(R.drawable.img_placeholder)
                .into(ivDetailPoster)
        }

        favoriteDetailViewModel.fetchListMeal()
        favoriteDetailViewModel.recommendationList.observe(this) { res ->
            binding.rvMeal.apply {
                adapter = recommendationAdapter
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(
                        this@FavoriteDetailActivity,
                        LinearLayoutManager.HORIZONTAL,
                        false
                    )
                recommendationAdapter.setData(res.data?.meals)
            }
            recommendationAdapter.setOnItemClickCallback(object :
                RecommendationAdapter.IOnItemCallBack {
                override fun onItemClickCallback(data: MealsItemList) {
                    val detail = Intent(this@FavoriteDetailActivity, DetailActivity::class.java)
                    detail.putExtra(DetailActivity.EXTRA_MEAL, data)
                    startActivity(detail)
                }

            })
        }

        binding.btnRemoveFavorite.setOnClickListener {
            deleteFavoriteMeal(favoriteMeal)
            val delete = Intent(this, FavoriteActivity::class.java)
            startActivity(delete)
            finish()
        }

    }

    private fun deleteFavoriteMeal(mealEntity: MealEntity?) {
        favoriteDetailViewModel.deleteFavoriteMeal(mealEntity)
        Toast.makeText(this, "Successfully remove from favorite", Toast.LENGTH_SHORT).show()
    }

    companion object {
        const val EXTRA_FAVORITE_MEAL = "favorite_meal"
    }
}