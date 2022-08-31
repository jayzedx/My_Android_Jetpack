package com.tutorial.chapter1.myapplication.domain

import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository

class GetSortedRestaurantUseCase {
    private val repository: RestaurantRepository = RestaurantRepository()
    suspend operator fun invoke(): List<Restaurant> {
        return repository.getRestaurants()
            .sortedBy { it.title }
    }
}