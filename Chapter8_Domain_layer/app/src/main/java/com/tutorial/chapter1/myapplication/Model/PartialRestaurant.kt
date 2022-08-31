package com.tutorial.chapter1.myapplication.Model

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class PartialRestaurant(
    @ColumnInfo(name = "r_id")
    val id: Int,

    //used val instead to var, we want to promote immutability and ensure recomposition events happen
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)
