package com.dimasnoufal.dimasnoufal_mealdb.data.network

import com.dimasnoufal.dimasnoufal_mealdb.data.network.api.MealApi
import com.dimasnoufal.dimasnoufal_mealdb.utils.AppConstant.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service {
    private val retrofit by lazy {
        Retrofit
            .Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val MealService: MealApi by lazy {
        retrofit.create(MealApi::class.java)
    }
}