package com.dimasnoufal.dimasnoufal_mealdb.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.dimasnoufal.dimasnoufal_mealdb.data.LocalDataSource
import com.dimasnoufal.dimasnoufal_mealdb.data.RemoteDataSource
import com.dimasnoufal.dimasnoufal_mealdb.data.Repository
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealDatabase
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import com.dimasnoufal.dimasnoufal_mealdb.data.network.Service
import com.dimasnoufal.dimasnoufal_mealdb.data.network.handler.NetworkResult
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealId
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    // Api
    private val remoteService = Service.MealService
    private val remote = RemoteDataSource(remoteService)

    // LOCAL
    private val mealDao = MealDatabase.getDatabase(application).mealDao()
    private val local = LocalDataSource(mealDao)

    private val repository = Repository(remote,local)

    private var _mealId: MutableLiveData<NetworkResult<ResponseMealId>> = MutableLiveData()
    val mealId: LiveData<NetworkResult<ResponseMealId>> = _mealId

    private var _recommendationList: MutableLiveData<NetworkResult<ResponseMealList>> = MutableLiveData()
    val recommendationList: LiveData<NetworkResult<ResponseMealList>> = _recommendationList

    fun fetchMealDetail(i: Int) {
        viewModelScope.launch {
            repository.remote!!.getMealId(i).collect { res ->
                _mealId.value = res
            }
        }
    }

    fun fetchListMeal() {
        viewModelScope.launch {
            repository.remote!!.getMealList().collect { res ->
                _recommendationList.value = res
            }
        }
    }

    val favoriteMealList:LiveData<List<MealEntity>> = repository.local!!.listMeal().asLiveData()

    fun insertFavoriteMeal(mealEntity: MealEntity){
        viewModelScope.launch(Dispatchers.IO) {
            repository.local!!.insertMeal(mealEntity)
        }
    }

    fun deleteFavoriteMeal(mealEntity: MealEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local!!.deleteMeal(mealEntity)
        }
    }

}