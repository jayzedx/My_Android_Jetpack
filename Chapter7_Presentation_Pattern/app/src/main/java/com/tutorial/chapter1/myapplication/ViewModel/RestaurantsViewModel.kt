package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.chapter1.myapplication.Database.RestaurantDb
import com.tutorial.chapter1.myapplication.Model.PartialRestaurant
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Model.RestaurantScreenState
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants
import com.tutorial.chapter1.myapplication.Network.RestaurantsApiService
import com.tutorial.chapter1.myapplication.RestaurantApplication
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RestaurantsViewModel() : ViewModel() {


    private var restInterface: RestaurantsApiService
    private lateinit var restaurantsCall: Call<List<Restaurant>>

    //val state: MutableState<List<Restaurant>> = mutableStateOf(dummyRestaurants.restoreSelections())
    //fun getRestaurants() = dummyRestaurants

    val state: MutableState<RestaurantScreenState> = mutableStateOf(
        RestaurantScreenState(restaurants = listOf(),isLoading = true)
    )

    //coroutines, async with io thread
    val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        state.value = state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    private var restaurantsDao = RestaurantDb
        .getDaoInstance(RestaurantApplication.getAppContext())


    init {
        val retrofit: Retrofit = Retrofit.Builder()
            //explicitly tell Retrofit that we want the JSON to be deserialized with the GSON converter, following the @Serialized
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-827eb-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(RestaurantsApiService::class.java)

        //triggering network requests for preventing side effect from recomposition (alternative)
        //getRestaurants()
    }

    private suspend fun getRemoteRestaurants() : List<Restaurant> {
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

    fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getRemoteRestaurants()
            //specific that works on main thread
            withContext(Dispatchers.Main) {
                state.value = state.value.copy(
                    restaurants = restaurants,
                    isLoading = false)
            }
        }
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleFavoriteRestaurant(id, oldValue)
            state.value = state.value.copy(restaurants = updatedRestaurants)
        }
    }

    private suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialRestaurant(
                    id = id,
                    isFavorite = !oldValue
                )
            )
            restaurantsDao.getAll()
        }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}