package com.tutorial.chapter1.myapplication.domain

import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository
import javax.inject.Inject

class ToggleRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val getSortedRestaurantUseCase: GetSortedRestaurantUseCase
) {

    suspend operator fun invoke(
        id: Int,
        oldValue: Boolean
    ): List<Restaurant> {
        val newFav = oldValue.not()
        repository.toggleFavoriteRestaurant(id, newFav)
        // don't need to call api again
        return getSortedRestaurantUseCase()
    }
}