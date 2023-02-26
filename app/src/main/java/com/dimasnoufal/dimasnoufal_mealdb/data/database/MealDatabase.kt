package com.dimasnoufal.dimasnoufal_mealdb.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [MealEntity::class],
    version = 2,
    exportSchema = false
)

@TypeConverters(MealTypeConverter::class)
abstract class MealDatabase : RoomDatabase() {

    abstract fun mealDao(): MealDao

    companion object {
        @Volatile
        private var instance: MealDatabase? = null

        fun getDatabase(ctx: Context): MealDatabase {
            val tempInstance = instance
            if (tempInstance != null) {
                instance = tempInstance
            }

            synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    ctx.applicationContext, MealDatabase::class.java,
                    "meal_database"
                ).fallbackToDestructiveMigration()
                    .build()

                instance = newInstance
                return newInstance
            }
        }
    }
}