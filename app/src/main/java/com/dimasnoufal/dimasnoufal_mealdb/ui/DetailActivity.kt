package com.dimasnoufal.dimasnoufal_mealdb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.dimasnoufal.dimasnoufal_mealdb.R
import com.dimasnoufal.dimasnoufal_mealdb.adapter.RecommendationAdapter
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import com.dimasnoufal.dimasnoufal_mealdb.data.network.handler.NetworkResult
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ActivityDetailBinding
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemId
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemList
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealId
import com.dimasnoufal.dimasnoufal_mealdb.viewmodels.DetailViewModel
import kotlin.math.log

class DetailActivity : AppCompatActivity() {

    private val detailViewModel by viewModels<DetailViewModel>()
    private lateinit var binding: ActivityDetailBinding
    private var mealId: MealsItemId? = null
    private val recommendationAdapter by lazy { RecommendationAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setTitle("Detail Meal")

        val meal = intent.getParcelableExtra<MealsItemList>(EXTRA_MEAL)

        Log.d("MealId", meal!!.idMeal.toString())

        detailViewModel.fetchMealDetail(meal?.idMeal!!.toInt())
        detailViewModel.fetchListMeal()

        detailViewModel.mealId.observe(this@DetailActivity) { res ->
            when (res) {
                is NetworkResult.Loading -> handleUi(
                    rvwrapper = false,
                    progressbar = true,
                    tverror = false,
                    iverror = false
                )
                is NetworkResult.Error -> {
                    handleUi(
                        rvwrapper = false,
                        progressbar = false,
                        tverror = true,
                        iverror = true
                    )
                    Toast.makeText(this@DetailActivity, res.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    val result = res.data!!.meals
                    for (mealDetail in result!!) {
                        mealId = mealDetail
                        Log.d("apiServiceSuccesActivity", mealDetail.toString())
                        binding.mealDetail = mealDetail
                        binding.apply {
                            Glide.with(this@DetailActivity)
                                .load(mealDetail?.strMealThumb)
                                .error(R.drawable.img_placeholder)
                                .into(ivDetailPoster)
                        }
                    }
                    handleUi(
                        rvwrapper = true,
                        progressbar = false,
                        tverror = false,
                        iverror = false
                    )
                }
            }
        }

        detailViewModel.recommendationList.observe(this) { res ->
            binding.rvMeal.apply {
                adapter = recommendationAdapter
                setHasFixedSize(true)
                layoutManager =
                    LinearLayoutManager(this@DetailActivity, LinearLayoutManager.HORIZONTAL, false)
                recommendationAdapter.setData(res.data?.meals)
            }
            recommendationAdapter.setOnItemClickCallback(object :
                RecommendationAdapter.IOnItemCallBack {
                override fun onItemClickCallback(data: MealsItemList) {
                    val detail = Intent(this@DetailActivity, DetailActivity::class.java)
                    detail.putExtra(DetailActivity.EXTRA_MEAL, data)
                    startActivity(detail)
                }

            })
        }

        isFavoriteMeal(meal)
    }

    private fun isFavoriteMeal(mealSelected: MealsItemList) {
        detailViewModel.favoriteMealList.observe(this@DetailActivity) { res ->
            val meal = res.find { fav ->
                fav.meal!!.idMeal == mealSelected.idMeal
            }
            if (meal != null) {
                binding.btnAddFavorite.apply {
                    setText(R.string.remove_from_favorite)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.red
                        )
                    )
                    setOnClickListener {
                        deleteFavoriteMeal(meal.id)
                    }
                }
            } else {
                binding.btnAddFavorite.apply {
                    setText(R.string.add_to_favorite)
                    setBackgroundColor(
                        ContextCompat.getColor(
                            this@DetailActivity,
                            R.color.light_brown
                        )
                    )
                    setOnClickListener {
                            insertFavoriteGame()
                    }
                }
            }
        }
    }

    private fun deleteFavoriteMeal(mealEntityId: Int) {
        val mealEntity = MealEntity(mealEntityId, mealId)
        detailViewModel.deleteFavoriteMeal(mealEntity)
        Toast.makeText(this, "Successfully remove from favorite", Toast.LENGTH_SHORT).show()
    }

    private fun insertFavoriteGame() {
        Log.d("test", mealId.toString())
        val mealEntity = MealEntity(meal = mealId)
        detailViewModel.insertFavoriteMeal(mealEntity)
        Toast.makeText(this, "Successfully added to favorite", Toast.LENGTH_SHORT).show()
    }

    private fun handleUi(
        rvwrapper: Boolean,
        progressbar: Boolean,
        tverror: Boolean,
        iverror: Boolean
    ) {
        binding.apply {
            clWrapper.isVisible = rvwrapper
            progressBar.isVisible = progressbar
            tvError.isVisible = tverror
            ivEmpty.isVisible = iverror
        }
    }

    companion object {
        const val EXTRA_MEAL = "meal"
    }

}