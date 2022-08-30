package com.tutorial.chapter1.myapplication.Database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutorial.chapter1.myapplication.Model.Restaurant

interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)
}