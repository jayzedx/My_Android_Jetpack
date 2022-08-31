package com.tutorial.chapter1.myapplication.domain

import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository

class ToggleRestaurantUseCase {
    private val repository: RestaurantRepository = RestaurantRepository()
    suspend operator fun invoke(
        id: Int,
        oldValue: Boolean
    ): List<Restaurant> {
        val newFav = oldValue.not()
        repository.toggleFavoriteRestaurant(id, newFav)
        // don't need to call api again
        return GetSortedRestaurantUseCase().invoke()
    }
}