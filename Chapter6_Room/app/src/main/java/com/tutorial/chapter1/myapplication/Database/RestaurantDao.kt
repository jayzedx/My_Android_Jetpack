package com.tutorial.chapter1.myapplication.Database

import androidx.room.*
import com.tutorial.chapter1.myapplication.Model.PartialRestaurant
import com.tutorial.chapter1.myapplication.Model.Restaurant

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)

    @Update(entity = Restaurant::class)
    suspend fun update(partialRestaurant: PartialRestaurant)

    @Update(entity = Restaurant::class)
    suspend fun updateAll(partialRestaurants: List<PartialRestaurant>)

    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
    suspend fun getAllFavorited(): List<Restaurant>
}