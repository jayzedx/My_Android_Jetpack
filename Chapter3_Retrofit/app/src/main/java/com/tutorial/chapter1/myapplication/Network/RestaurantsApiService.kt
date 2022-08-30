package com.tutorial.chapter1.myapplication.Network

import com.tutorial.chapter1.myapplication.Model.Restaurant
import retrofit2.Call
import retrofit2.http.GET

interface RestaurantsApiService {
    @GET("restaurants.json")
    fun getRestaurants(): Call<List<Restaurant>>
}