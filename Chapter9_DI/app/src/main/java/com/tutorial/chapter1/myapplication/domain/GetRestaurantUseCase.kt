package com.tutorial.chapter1.myapplication.domain

import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository
import javax.inject.Inject

class GetRestaurantUseCase @Inject constructor(
    private val repository: RestaurantRepository,
    private val getSortedRestaurantUseCase: GetSortedRestaurantUseCase)
{
    suspend operator fun invoke(): List<Restaurant> {
        repository.loadRestaurants()
        return getSortedRestaurantUseCase()
    }
}