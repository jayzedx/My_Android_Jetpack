package com.tutorial.chapter1.myapplication.Screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tutorial.chapter1.myapplication.Component.RestaurantItem
import com.tutorial.chapter1.myapplication.Model.Restaurant
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants
import com.tutorial.chapter1.myapplication.ViewModel.RestaurantsViewModel
import com.tutorial.chapter1.myapplication.ui.theme.MyApplicationTheme

@Composable
fun RestaurantScreen(onItemClick: (id: Int) -> Unit = {}) {
    //    Column(Modifier.verticalScroll(rememberScrollState())) {
    //        dummyRestaurants.forEach { item ->
    //            RestaurantItem(item)
    //        }
    //    }

    val viewModel: RestaurantsViewModel = viewModel()
    val state = viewModel.state.value

    //triggering network requests for preventing side effect from recomposition
    LaunchedEffect(key1 = "request_restaurants") {
        viewModel.getRestaurants()
    }
    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {

        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
            items(state.restaurants) { restaurant ->
                RestaurantItem(restaurant,
                    onFavoriteClick = { id, oldValue ->
                        viewModel.toggleFavorite(id, oldValue)
                    },
                    onItemClick = { id ->
                        onItemClick(id)
                    },
                )
            }
        }

        if(state.isLoading)
            CircularProgressIndicator()
        if (state.error != null)
            Text(state.error)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        RestaurantScreen()
    }
}