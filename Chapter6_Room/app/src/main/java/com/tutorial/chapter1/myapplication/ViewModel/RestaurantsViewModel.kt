package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tutorial.chapter1.myapplication.Database.RestaurantDb
import com.tutorial.chapter1.myapplication.Model.PartialRestaurant
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants
import com.tutorial.chapter1.myapplication.Network.RestaurantsApiService
import com.tutorial.chapter1.myapplication.RestaurantApplication
import kotlinx.coroutines.*
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {


    private var restInterface: RestaurantsApiService
    private lateinit var restaurantsCall: Call<List<Restaurant>>

    //val state: MutableState<List<Restaurant>> = mutableStateOf(dummyRestaurants.restoreSelections())
    //fun getRestaurants() = dummyRestaurants

    val state: MutableState<List<Restaurant>> = mutableStateOf(emptyList<Restaurant>())

    //coroutines, async with io thread
    val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)
    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        exception.printStackTrace()
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
                val restaurants = restInterface.getRestaurants()
                restaurantsDao.addAll(restaurants)
                return@withContext restaurants
            } catch (e: Exception) {
                when (e) {
                    is SocketTimeoutException,
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        return@withContext restaurantsDao.getAll()
                    }
                    else -> throw e
                }
            }
        }
    }

    fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getRemoteRestaurants()
            //specific that works on main thread
            withContext(Dispatchers.Main) {
                state.value = restaurants.restoreSelections()
            }
        }
    }

    fun toggleFavorite(id: Int) {
        val restaurants = state.value.toMutableList()
        val itemIndex =
            restaurants.indexOfFirst { it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] =
            item.copy(isFavorite = !item.isFavorite)
        storeSelection(restaurants[itemIndex])
        state.value = restaurants

        viewModelScope.launch {
            toggleFavoriteRestaurant(id, item.isFavorite)
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
        }

    private fun storeSelection(item: Restaurant) {
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }

    private fun List<Restaurant>.restoreSelections(): List<Restaurant> {
        stateHandle.get<List<Int>?>(FAVORITES)?.let { selectedIds ->
            val restaurantsMap = this.associateBy { it.id }
                .toMutableMap()// creating Map type variable with using id as key
            selectedIds.forEach { id ->
                val restaurant = restaurantsMap[id] ?: return@forEach
                //copy() function), will be notified so that it can trigger recomposition
                restaurantsMap[id] = restaurant.copy(isFavorite = true)
            }
            return restaurantsMap.values.toList()
        }
        return this //return original list
    }


    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }


    companion object {
        const val FAVORITES = "favorites"
    }
}