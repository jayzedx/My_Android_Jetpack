package com.tutorial.chapter1.myapplication.presentation.list

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.chapter1.myapplication.data.local.RestaurantDb
import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository
import com.tutorial.chapter1.myapplication.RestaurantApplication
import com.tutorial.chapter1.myapplication.data.di.PermissionUtils
import com.tutorial.chapter1.myapplication.domain.GetRestaurantUseCase
import com.tutorial.chapter1.myapplication.domain.ToggleRestaurantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import javax.inject.Inject

@HiltViewModel
class RestaurantsViewModel @Inject constructor(
    private val permissionUtils: PermissionUtils,
    private val getRestaurantUseCase: GetRestaurantUseCase,
    private val toggleRestaurantUseCase: ToggleRestaurantUseCase
) : ViewModel() {

    //val state: MutableState<List<Restaurant>> = mutableStateOf(dummyRestaurants.restoreSelections())
    //fun getRestaurants() = dummyRestaurants

    private val _state: MutableState<RestaurantScreenState> = mutableStateOf(
        RestaurantScreenState(restaurants = listOf(), isLoading = true)
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



    init {
        //triggering network requests for preventing side effect from recomposition (alternative)
        getRestaurants()
    }


    fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getRestaurantUseCase()
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
            val updatedRestaurants = toggleRestaurantUseCase(id, oldValue)
            _state.value = _state.value.copy(restaurants = updatedRestaurants)
        }
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


}