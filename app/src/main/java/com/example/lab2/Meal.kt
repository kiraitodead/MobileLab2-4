package com.example.lab2
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meals")
data class Meal(@PrimaryKey val title: String, val image: String, val recipeID: String)
