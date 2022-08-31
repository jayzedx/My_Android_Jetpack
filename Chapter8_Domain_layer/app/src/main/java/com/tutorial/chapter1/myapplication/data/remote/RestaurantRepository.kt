package com.tutorial.chapter1.myapplication.data.remote

import com.tutorial.chapter1.myapplication.data.local.RestaurantDb
import com.tutorial.chapter1.myapplication.data.local.PartialRestaurant
import com.tutorial.chapter1.myapplication.RestaurantApplication
import com.tutorial.chapter1.myapplication.domain.Restaurant
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RestaurantRepository {
    private var restInterface: RestaurantsApiService =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-827eb-default-rtdb.firebaseio.com/")
            .build()
            .create(RestaurantsApiService::class.java)

    private var restaurantsDao = RestaurantDb
        .getDaoInstance(RestaurantApplication.getAppContext())


    suspend fun toggleFavoriteRestaurant(id: Int, value: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialRestaurant(
                    id = id,
                    isFavorite = value
                )
            )
        }

    suspend fun loadRestaurants() : List<Restaurant> {
        return withContext(Dispatchers.IO) {
            try {
                refreshCache()
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException,
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        if (restaurantsDao.getAll().isEmpty())
                            throw Exception("Something went wrong. We have no data.")
                    }
                    else -> throw e
                }
            }
            return@withContext restaurantsDao.getAll()
        }
    }

    private suspend fun refreshCache() {
        val remoteRestaurants = restInterface.getRestaurants()
        val favoriteRestaurants = restaurantsDao.getAllFavorited()

        restaurantsDao.addAll(remoteRestaurants)
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialRestaurant(it.id, true)
            })
    }

    suspend fun getRestaurants() : List<Restaurant> {
        return withContext(Dispatchers.IO) {
            return@withContext restaurantsDao.getAll()
        }
    }
}