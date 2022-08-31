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
import com.tutorial.chapter1.myapplication.Network.RestaurantRepository
import com.tutorial.chapter1.myapplication.Network.RestaurantsApiService
import com.tutorial.chapter1.myapplication.RestaurantApplication
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RestaurantsViewModel() : ViewModel() {

    private val repository = RestaurantRepository()

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
        //triggering network requests for preventing side effect from recomposition (alternative)
        //getRestaurants()
    }


    fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = repository.getRemoteRestaurants()
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
            val updatedRestaurants = repository.toggleFavoriteRestaurant(id, oldValue)
            state.value = state.value.copy(restaurants = updatedRestaurants)
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}