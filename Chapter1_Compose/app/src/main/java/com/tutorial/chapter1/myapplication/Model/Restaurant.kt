package com.tutorial.chapter1.myapplication.Model

data class Restaurant(val id: Int, val title: String, val description: String)

val dummyRestaurants = listOf(
    Restaurant(0, "Alfredo foods", "At Alfredo's …"),
    Restaurant(1, "Jay or Jim Shopping", ""),
    Restaurant(2, "Mike and Ben's food pub", "")
)