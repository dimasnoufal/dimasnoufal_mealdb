package com.dimasnoufal.dimasnoufal_mealdb.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.dimasnoufal.dimasnoufal_mealdb.data.LocalDataSource
import com.dimasnoufal.dimasnoufal_mealdb.data.Repository
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealDatabase
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    // LOCAL
    private val mealDao = MealDatabase.getDatabase(application).mealDao()
    private val local = LocalDataSource(mealDao)

    private val repository = Repository(local = local)

    val favoriteMealList: LiveData<List<MealEntity>> = repository.local!!.listMeal().asLiveData()
}