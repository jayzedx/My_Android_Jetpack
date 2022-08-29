package com.tutorial.chapter1.myapplication.ViewModel

import androidx.lifecycle.ViewModel
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants

class RestaurantsViewModel(): ViewModel() {
    fun getRestaurants() = dummyRestaurants
}