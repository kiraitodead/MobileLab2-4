package com.example.lab2

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface MealDao {
    @Insert
    fun insert(meal: List<Meal>)

    @Query("DELETE FROM meals")
    fun delete()

    @Query("SELECT * FROM meals")
    fun getAllMeals(): List<Meal>
}

@Dao
interface CategoryDao {
    @Insert
    fun insert(category: List<Category>)

    @Query("DELETE FROM categories")
    fun delete()

    @Query("SELECT * FROM categories")
    fun getAllCategories(): List<Category>
}

@Dao
interface RecipeDao {
    @Insert
    fun insert(recipe: List<Recipe>)

    @Query("DELETE FROM recipes")
    fun delete()

    @Query("SELECT * FROM recipes")
    fun getAllRecipes(): List<Recipe>
}