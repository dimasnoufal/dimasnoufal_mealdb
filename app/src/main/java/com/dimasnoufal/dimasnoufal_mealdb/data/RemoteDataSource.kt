package com.dimasnoufal.dimasnoufal_mealdb.data

import android.util.Log
import com.dimasnoufal.dimasnoufal_mealdb.data.network.api.MealApi
import com.dimasnoufal.dimasnoufal_mealdb.data.network.handler.NetworkResult
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemId
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealId
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class RemoteDataSource(private val mealApi: MealApi) {
    suspend fun getMealList(): Flow<NetworkResult<ResponseMealList>> = flow {
        try {
            emit(NetworkResult.Loading(true))
            val mealList = mealApi.getMealList()

            // Request data successfull
            if (mealList.isSuccessful) {
                val responseBody = mealList.body()

                // if data empty
                if (responseBody?.meals?.isEmpty() == true) {
                    emit(NetworkResult.Error("Seafood list not found"))
                } else {
                    emit(NetworkResult.Success(responseBody!!))
                }
            } else {
                // request data failed
                Log.d(
                    "apiServiceError",
                    "statusCode:${mealList.code()}, message:${mealList.message()}"
                )
                emit(NetworkResult.Error("Failed to fetch data from server."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("remoteError", "${e.message}")
            emit(NetworkResult.Error("Something went wrong. Please check log."))
        }
    }.flowOn(Dispatchers.IO)

    suspend fun getMealId(i: Int): Flow<NetworkResult<ResponseMealId>> = flow {
        try {
            emit(NetworkResult.Loading(true))
            val mealId = mealApi.getMealId(i)

            // Request data successfull
            if (mealId.isSuccessful) {
                val responseBody = mealId.body()
                val result = responseBody?.meals
                for (i in result!!) {
                    Log.d("apiServiceSucces", i.toString())
                }

                // if data empty
                if (responseBody != null) {
                    Log.d("apiServiceSucces", responseBody.toString())
                    emit(NetworkResult.Success(responseBody))
                } else {
                    emit(NetworkResult.Error("Can't fetch detail meal."))
                }
            } else {
                // request data failed
                Log.d(
                    "apiServiceError",
                    "statusCode:${mealId.code()}, message:${mealId.message()}"
                )
                emit(NetworkResult.Error("Failed to fetch data from server."))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("remoteError", "${e.message}")
            emit(NetworkResult.Error("Something went wrong. Please check log."))
        }
    }.flowOn(Dispatchers.IO)
}