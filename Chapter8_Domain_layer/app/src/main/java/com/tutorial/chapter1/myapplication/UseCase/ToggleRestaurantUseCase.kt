package com.tutorial.chapter1.myapplication.UseCase

import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Network.RestaurantRepository

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