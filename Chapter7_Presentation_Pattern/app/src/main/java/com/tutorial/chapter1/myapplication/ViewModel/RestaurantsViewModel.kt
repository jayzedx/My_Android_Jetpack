package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
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
import com.tutorial.chapter1.myapplication.UseCase.GetRestaurantUseCase
import com.tutorial.chapter1.myapplication.UseCase.ToggleRestaurantUseCase
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

    private val _state: MutableState<RestaurantScreenState> = mutableStateOf(
        RestaurantScreenState(restaurants = listOf(),isLoading = true)
    )
    val state: State<RestaurantScreenState>
        get() = _state

    //coroutines, async with io thread
    val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
        _state.value = _state.value.copy(
            error = exception.message,
            isLoading = false
        )
    }

    private var restaurantsDao = RestaurantDb
        .getDaoInstance(RestaurantApplication.getAppContext())

    private val getRestaurantsUseCase = GetRestaurantUseCase()
    private val toggleRestaurantsUseCase = ToggleRestaurantUseCase()

    init {
        //triggering network requests for preventing side effect from recomposition (alternative)
        //getRestaurants()
    }


    fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getRestaurantsUseCase()
            //specific that works on main thread
            withContext(Dispatchers.Main) {
                _state.value = _state.value.copy(
                    restaurants = restaurants,
                    isLoading = false)
            }
        }
    }

    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch {
            val updatedRestaurants = toggleRestaurantsUseCase(id, oldValue)
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}