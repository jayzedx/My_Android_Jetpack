package com.tutorial.chapter1.myapplication.Screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.tutorial.chapter1.myapplication.Component.RestaurantItem
import com.tutorial.chapter1.myapplication.ui.theme.MyApplicationTheme

@Composable
fun RestaurantScreen() {
    RestaurantItem()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        RestaurantScreen()
    }
}