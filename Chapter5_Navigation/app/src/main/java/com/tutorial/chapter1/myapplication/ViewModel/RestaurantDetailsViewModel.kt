package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Network.RestaurantsApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantDetailsViewModel() : ViewModel() {
    private var restInterface: RestaurantsApiService
    val state = mutableStateOf<Restaurant?>(null)

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory
                .create())
            .baseUrl("https://restaurants-827eb-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(
            RestaurantsApiService::class.java)

        viewModelScope.launch {
            val restaurant = getRemoteRestaurant(2)
            state.value = restaurant
        }
    }

    private suspend fun getRemoteRestaurant(id: Int):
            Restaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface
                .getRestaurant(id)
            return@withContext responseMap.values.first()
        }
    }
}