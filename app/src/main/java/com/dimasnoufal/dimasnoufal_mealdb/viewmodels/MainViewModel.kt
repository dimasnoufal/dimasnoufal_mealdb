package com.dimasnoufal.dimasnoufal_mealdb.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dimasnoufal.dimasnoufal_mealdb.data.RemoteDataSource
import com.dimasnoufal.dimasnoufal_mealdb.data.Repository
import com.dimasnoufal.dimasnoufal_mealdb.data.network.Service
import com.dimasnoufal.dimasnoufal_mealdb.data.network.handler.NetworkResult
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealList
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {

    // Api
    private val remoteService = Service.MealService
    private val remote = RemoteDataSource(remoteService)

    private val repository = Repository(remote)

    private var _mealList: MutableLiveData<NetworkResult<ResponseMealList>> = MutableLiveData()
    val mealList: LiveData<NetworkResult<ResponseMealList>> = _mealList

    init {
        fetchListMeal()
    }

    private fun fetchListMeal() {
        viewModelScope.launch {
            repository.remote!!.getMealList().collect { res ->
                _mealList.value = res
            }
        }
    }
}