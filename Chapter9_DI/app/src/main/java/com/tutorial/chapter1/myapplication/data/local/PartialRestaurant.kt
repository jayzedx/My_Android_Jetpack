package com.tutorial.chapter1.myapplication.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class PartialLocalRestaurant(
    @ColumnInfo(name = "r_id")
    val id: Int,

    //used val instead to var, we want to promote immutability and ensure recomposition events happen
    @ColumnInfo(name = "is_favorite")
    val isFavorite: Boolean
)
