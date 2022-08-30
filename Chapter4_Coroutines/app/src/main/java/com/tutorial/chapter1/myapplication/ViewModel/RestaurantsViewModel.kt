package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants
import com.tutorial.chapter1.myapplication.Network.RestaurantsApiService
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {


    private var restInterface: RestaurantsApiService
    private lateinit var restaurantsCall: Call<List<Restaurant>>

    //val state: MutableState<List<Restaurant>> = mutableStateOf(dummyRestaurants.restoreSelections())
    //fun getRestaurants() = dummyRestaurants

    val state: MutableState<List<Restaurant>> = mutableStateOf(emptyList<Restaurant>())

    //coroutines, async with io thread
    val job = Job()
    private val scope = CoroutineScope(job + Dispatchers.IO)

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

    fun getRestaurants() {
       scope.launch {
           val restaurants = restInterface.getRestaurants()
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
            val restaurantsMap = this.associateBy { it.id } // creating Map type variable with using id as key
            selectedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
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