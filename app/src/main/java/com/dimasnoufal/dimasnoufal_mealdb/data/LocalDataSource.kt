package com.dimasnoufal.dimasnoufal_mealdb.data

import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealDao
import com.dimasnoufal.dimasnoufal_mealdb.data.database.MealEntity
import kotlinx.coroutines.flow.Flow

class LocalDataSource(private val dao: MealDao) {
    suspend fun insertMeal(mealEntity: MealEntity) = dao.insertMeal(mealEntity)

    fun listMeal(): Flow<List<MealEntity>> = dao.listMeal()

    suspend fun deleteMeal(mealEntity: MealEntity?) = dao.deleteMeal(mealEntity)

    suspend fun deleteAllMeal() = dao.deleteAllMeal()
}