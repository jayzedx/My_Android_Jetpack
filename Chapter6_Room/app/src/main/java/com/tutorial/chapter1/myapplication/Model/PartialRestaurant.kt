package com.tutorial.chapter1.myapplication.Model

import androidx.room.ColumnInfo

data class PartialRestaurant(
    @ColumnInfo(name = "r_id")
    val id: Int,
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)
