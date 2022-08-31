package com.tutorial.chapter1.myapplication.domain
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

data class Restaurant(
    val id: Int,
    val title: String,
    val description: String,
    val isFavorite: Boolean = false
)

val dummyRestaurants = listOf(
    Restaurant(0, "Alfredo foods", "At Alfredo's …"),
    Restaurant(1, "Jay or Jim Shopping", ""),
    Restaurant(2, "Mike and Ben's food pub", ""),
    Restaurant(3, "Alfredo foods", "At Alfredo's …"),
    Restaurant(4, "Jay or Jim Shopping", ""),
    Restaurant(5, "Mike and Ben's food pub", ""),
    Restaurant(6, "Alfredo foods", "At Alfredo's …"),
    Restaurant(7, "Jay or Jim Shopping", ""),
    Restaurant(8, "Mike and Ben's food pub", ""),
    Restaurant(9, "Jay or Jim Shopping", ""),
    Restaurant(10, "Mike and Ben's food pub", ""),
    Restaurant(11, "Alfredo foods", "At Alfredo's …"),
    Restaurant(12, "Jay or Jim Shopping", ""),
    Restaurant(13, "Mike and Ben's food pub", ""),
    Restaurant(14, "Alfredo foods", "At Alfredo's …"),
    Restaurant(15, "Jay or Jim Shopping", ""),
    Restaurant(16, "Mike and Ben's food pub", "")
)