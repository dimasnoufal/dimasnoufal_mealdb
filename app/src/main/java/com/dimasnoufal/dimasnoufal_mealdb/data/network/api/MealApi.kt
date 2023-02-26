package com.dimasnoufal.dimasnoufal_mealdb.data.network.api

import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemId
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealId
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MealApi {
    @GET("filter.php?c=Seafood")
    suspend fun getMealList() : Response<ResponseMealList>

    @GET("lookup.php?")
    suspend fun getMealId(@Query("i") i: Int) : Response<ResponseMealId>
}