package com.tutorial.chapter1.myapplication.Network

import com.tutorial.chapter1.myapplication.Model.Restaurant
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface RestaurantsApiService {
    @GET("restaurants.json")
//  use coroutines instead of callbacks
    suspend fun getRestaurants() : List<Restaurant>

//    @POST("user/edit")
//    fun updateUser(@Field("first_name") firstName: String): Call<User>
}