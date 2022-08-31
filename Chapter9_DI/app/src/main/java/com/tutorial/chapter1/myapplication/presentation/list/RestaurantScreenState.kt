package com.tutorial.chapter1.myapplication.presentation.list

import com.tutorial.chapter1.myapplication.domain.Restaurant

data class RestaurantScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean,
    val error: String? = null)