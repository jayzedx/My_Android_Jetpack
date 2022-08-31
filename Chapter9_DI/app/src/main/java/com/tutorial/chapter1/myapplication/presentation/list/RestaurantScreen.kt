package com.tutorial.chapter1.myapplication.presentation.list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tutorial.chapter1.myapplication.component.RestaurantItem
import com.tutorial.chapter1.myapplication.presentation.list.RestaurantScreenState
import com.tutorial.chapter1.myapplication.ui.theme.MyApplicationTheme

@Composable
fun RestaurantScreen(state: RestaurantScreenState,
                     onItemClick: (id: Int) -> Unit = {},
                     onFavoriteClick: (id: Int, oldValue: Boolean) -> Unit) {

    /*
    val viewModel: RestaurantsViewModel = viewModel()
    //triggering network requests for preventing side effect from recomposition
    LaunchedEffect(key1 = "request_restaurants") {
        viewModel.getRestaurants()
    }
    */

    Box(contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()) {

        LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
            items(state.restaurants) { restaurant ->
                RestaurantItem(restaurant,
                    onFavoriteClick = { id, oldValue ->
                        onFavoriteClick(id, oldValue)
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
        RestaurantScreen(
            RestaurantScreenState(listOf(), true),
            {},
            { _, _ -> }
        )
    }
}