package com.dimasnoufal.dimasnoufal_mealdb.data.database

import androidx.room.TypeConverter
import com.dimasnoufal.dimasnoufal_mealdb.model.MealsItemId
import com.dimasnoufal.dimasnoufal_mealdb.model.ResponseMealId
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MealTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun mealDataToString(meal: MealsItemId?): String {
        return gson.toJson(meal)
    }

    @TypeConverter
    fun mealStringToData(string: String): MealsItemId? {
        val listType = object : TypeToken<MealsItemId?>() {}.type
        return gson.fromJson(string, listType)
    }
}