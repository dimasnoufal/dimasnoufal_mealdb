package com.dimasnoufal.dimasnoufal_mealdb.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.dimasnoufal.dimasnoufal_mealdb.data.LocalDataSource
import com.dimasnoufal.dimasnoufal_mealdb.data.RemoteDataSource
import com.dimasnoufal.dimasnoufal_mealdb.data.Repository
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealDatabase
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import com.dimasnoufal.dimasnoufal_mealdb.data.network.Service
import com.dimasnoufal.dimasnoufal_mealdb.data.network.handler.NetworkResult
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteDetailViewModel(application: Application): AndroidViewModel(application) {
    // Api
    private val remoteService = Service.MealService
    private val remote = RemoteDataSource(remoteService)

    // LOCAL
    private val mealDao = MealDatabase.getDatabase(application).mealDao()
    private val local = LocalDataSource(mealDao)

    private val repository = Repository(remote,local)

    private var _recommendationList: MutableLiveData<NetworkResult<ResponseMealList>> = MutableLiveData()
    val recommendationList: LiveData<NetworkResult<ResponseMealList>> = _recommendationList


    fun fetchListMeal() {
        viewModelScope.launch {
            repository.remote!!.getMealList().collect { res ->
                _recommendationList.value = res
            }
        }
    }

    fun deleteFavoriteMeal(gameEntity: MealEntity?){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local!!.deleteMeal(gameEntity)
        }
    }
}