package com.example.lab2
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class Recipe(@PrimaryKey val title: String, val image: String, val content: String)
