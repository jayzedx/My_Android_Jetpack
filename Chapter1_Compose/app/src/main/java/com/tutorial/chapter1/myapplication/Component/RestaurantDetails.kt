package com.tutorial.chapter1.myapplication.Component

import androidx.compose.foundation.layout.Column
import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier

@Composable
fun RestaurantDetails(modifier: Modifier) {
    Column(modifier = modifier) {
        Text(
            text = "Alfredo's dishes",
            style = MaterialTheme.typography.h6
        )
        CompositionLocalProvider(
            LocalContentAlpha provides
                    ContentAlpha.medium
        ) {
            Text(
                text = "At Alfredo's … seafood dishes.",
                style = MaterialTheme.typography.body2
            )
        }

    }
}
