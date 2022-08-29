package com.tutorial.chapter1.myapplication.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tutorial.chapter1.myapplication.Component.RestaurantItem
import com.tutorial.chapter1.myapplication.Model.dummyRestaurants
import com.tutorial.chapter1.myapplication.ui.theme.MyApplicationTheme

@Composable
fun RestaurantScreen() {
    Column {
        dummyRestaurants.forEach { item ->
            RestaurantItem(item)
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