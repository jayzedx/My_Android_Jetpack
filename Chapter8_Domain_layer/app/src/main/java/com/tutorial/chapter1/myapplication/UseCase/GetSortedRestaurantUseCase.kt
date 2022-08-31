package com.tutorial.chapter1.myapplication.UseCase

import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Network.RestaurantRepository

class GetSortedRestaurantUseCase {
    private val repository: RestaurantRepository = RestaurantRepository()
    suspend operator fun invoke(): List<Restaurant> {
        return repository.getRestaurants()
            .sortedBy { it.title }
    }
}