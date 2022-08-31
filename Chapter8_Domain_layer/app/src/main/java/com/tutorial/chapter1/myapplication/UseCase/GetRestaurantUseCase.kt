package com.tutorial.chapter1.myapplication.UseCase

import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Network.RestaurantRepository

class GetRestaurantUseCase {
    private val repository: RestaurantRepository = RestaurantRepository()
    private val getSortedRestaurantsUseCase = GetSortedRestaurantUseCase()
    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantsUseCase()
    }
}