package com.dimasnoufal.dimasnoufal_mealdb.model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseMealList(

	@field:SerializedName("meals")
	val meals: List<MealsItemList?>? = null
) : Parcelable

@Parcelize
data class MealsItemList(

	@field:SerializedName("strMealThumb")
	val strMealThumb: String? = null,

	@field:SerializedName("idMeal")
	val idMeal: String? = null,

	@field:SerializedName("strMeal")
	val strMeal: String? = null
) : Parcelable
