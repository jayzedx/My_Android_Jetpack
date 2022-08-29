package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants

class RestaurantsViewModel(): ViewModel() {
    fun getRestaurants() = dummyRestaurants

    val state : MutableState<List<Restaurant>> = mutableStateOf(dummyRestaurants)

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex =
            restaurants.indexOfFirst { it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] =
            item.copy(isFavorite = !item.isFavorite)
        state.value = restaurants
    }
}