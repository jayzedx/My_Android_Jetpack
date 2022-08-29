package com.tutorial.chapter1.myapplication.Component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Place
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tutorial.chapter1.myapplication.Model.Restaurant


@Composable
fun RestaurantItem(item: Restaurant, onClick: (id: Int) -> Unit) {

    val icon = if (item.isFavorite)
        Icons.Filled.Favorite
    else
        Icons.Filled.FavoriteBorder


    Card(
        elevation = 8.dp,
        modifier = Modifier.padding(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            RestaurantIcon(Icons.Filled.Place, Modifier.weight(0.15f))
            RestaurantDetails(
                item.title, item.description,
                Modifier.weight(0.85f)
            )

            //FavoriteIcon(Modifier.weight(0.15f))
            RestaurantIcon(icon, Modifier.weight(0.15f)) {
                onClick(item.id)
            }
        }
    }
}
