package com.tutorial.chapter1.myapplication.ViewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants

class RestaurantsViewModel(private val stateHandle: SavedStateHandle) : ViewModel() {
    fun getRestaurants() = dummyRestaurants

    val state: MutableState<List<Restaurant>> = mutableStateOf(dummyRestaurants.restoreSelections())

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


    companion object {
        const val FAVORITES = "favorites"
    }
}