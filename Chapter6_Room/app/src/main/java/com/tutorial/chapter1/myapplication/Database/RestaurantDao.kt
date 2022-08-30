package com.tutorial.chapter1.myapplication.Database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tutorial.chapter1.myapplication.Model.Restaurant

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)
}