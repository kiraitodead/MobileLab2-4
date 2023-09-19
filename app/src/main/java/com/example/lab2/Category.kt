package com.example.lab2
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(@PrimaryKey val title: String, val image: String)
