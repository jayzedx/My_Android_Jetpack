package com.tutorial.chapter1.myapplication.domain

import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository
import javax.inject.Inject

class GetSortedRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository) {

    suspend operator fun invoke(): List<Restaurant> {
        return repository.getRestaurants()
            .sortedBy { it.title }
    }
}