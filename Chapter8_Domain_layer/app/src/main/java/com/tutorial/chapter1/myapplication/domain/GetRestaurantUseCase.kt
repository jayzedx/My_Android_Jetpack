package com.tutorial.chapter1.myapplication.domain

import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository

class GetRestaurantUseCase {
    private val repository: RestaurantRepository = RestaurantRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantUseCase()
    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}