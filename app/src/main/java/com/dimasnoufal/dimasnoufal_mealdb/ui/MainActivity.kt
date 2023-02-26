package com.dimasnoufal.dimasnoufal_mealdb.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.view.isVisible
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dimasnoufal.dimasnoufal_mealdb.R
import com.dimasnoufal.dimasnoufal_mealdb.adapter.MainAdapter
import com.dimasnoufal.dimasnoufal_mealdb.data.network.handler.NetworkResult
import com.dimasnoufal.dimasnoufal_mealdb.databinding.ActivityMainBinding
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemList
import com.dimasnoufal.dimasnoufal_mealdb.viewmodels.MainViewModel

class MainActivity : AppCompatActivity() {

    private val mainViewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding
    private var menu: Menu? = null
    private val mainAdapter by lazy { MainAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainViewModel.mealList.observe(this@MainActivity) { res ->
            when (res) {
                is NetworkResult.Loading -> {
                    handleUi(
                        recylerview = false,
                        progressbar = true,
                        tverror = false,
                        iverror = false
                    )
                }
                is NetworkResult.Error -> {
                    binding.tvError.text = res.errorMessage
                    handleUi(
                        recylerview = false,
                        progressbar = false,
                        tverror = true,
                        iverror = true
                    )
                    Toast.makeText(this@MainActivity, res.errorMessage, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Success -> {
                    binding.rvFavorite.apply {
                        adapter = mainAdapter
                        setHasFixedSize(true)
                        layoutManager = StaggeredGridLayoutManager(2, LinearLayout.VERTICAL)
                        mainAdapter.setData(res.data?.meals)
                    }
                    mainAdapter.setOnItemClickCallback(object : MainAdapter.IOnItemCallBack {
                        override fun onItemClickCallback(data: MealsItemList) {
                            val detail = Intent(this@MainActivity,DetailActivity::class.java)
                            detail.putExtra(DetailActivity.EXTRA_MEAL,data)
                            startActivity(detail)
                        }
                    })
                    handleUi(
                        recylerview = true,
                        progressbar = false,
                        tverror = false,
                        iverror = false
                    )
                }
            }
        }
    }

    private fun handleUi(
        recylerview: Boolean,
        progressbar: Boolean,
        tverror: Boolean,
        iverror: Boolean
    ) {
        binding.apply {
            rvFavorite.isVisible = recylerview
            progressBar.isVisible = progressbar
            tvError.isVisible = tverror
            ivError.isVisible = iverror
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorite_menu, menu)
        this.menu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val profile = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(profile)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}