package com.tutorial.chapter1.myapplication.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tutorial.chapter1.myapplication.Component.RestaurantItem
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants
import com.tutorial.chapter1.myapplication.ui.theme.MyApplicationTheme

@Composable
fun RestaurantScreen() {
    //    Column(Modifier.verticalScroll(rememberScrollState())) {
    //        dummyRestaurants.forEach { item ->
    //            RestaurantItem(item)
    //        }
    //    }
    LazyColumn(contentPadding = PaddingValues(vertical = 8.dp, horizontal = 8.dp)) {
        items(dummyRestaurants) { restaurant ->
            RestaurantItem(restaurant)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        RestaurantScreen()
    }
}