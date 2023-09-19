package com.example.lab2

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Category::class, Meal::class, Recipe::class], version = 1)
abstract class MainDb : RoomDatabase() {
    abstract fun categoryDao() : CategoryDao
    abstract fun mealDao() : MealDao
    abstract fun recipeDao() : RecipeDao

    companion object{
        fun getDb(context: Context): MainDb{
            return Room.databaseBuilder(
                context.applicationContext,
                MainDb::class.java,
                "labdb.db"
            ).build()
        }
    }
}