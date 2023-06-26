package com.example.tastemap.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import com.example.tastemap.TasteMapApp
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.ui.theme.TasteMapTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onSignOutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userName by remember { mutableStateOf("") }
    var restaurants by remember { mutableStateOf(emptyList<Restaurant>()) }

    LaunchedEffect(Unit) {
        viewModel.fetchUserDetails(
            onSuccess = { name -> userName = name }
        )
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("User Name: $userName")
            Button(
                onClick = {
                    viewModel.signOut()
                    onSignOutClicked()
                }
            ) {
                Text("Sign Out")
            }
            val dummyRequest = HotPepperApiRequest(
                lat = 33.652294,
                lng = 130.672144,
                range = 5,
                keyword = "",
                order = 4,
                count = 10
            )
            Button(
                onClick = {
                    viewModel.fetchRestaurants(
                        request = dummyRequest,
                        isSortSelected = true,
                        onSuccess = { response -> restaurants = response }
                    )
                }
            ) {
                Text("Search Restaurants")
            }
            val tmpRestaurants = listOf(Restaurant(name = "test", rating = 3.2, usrReviews = 4))
            RestaurantsList(restaurants = restaurants)
        }
    }

}

@Composable
fun RestaurantsList(restaurants: List<Restaurant>) {
    LazyColumn {
        items(restaurants) { restaurantDetail ->
            Restaurant(restaurantDetail)
        }
    }
}

@Composable
fun Restaurant(restaurantDetail: Restaurant) {
    Column() {
        Text("${restaurantDetail.name}")
        Spacer(modifier = Modifier.size(4.dp))
        Text("star: ${restaurantDetail.rating}, reviews: ${restaurantDetail.usrReviews}")
    }
}

@Preview
@Composable
fun RestaurantPReview() {
    TasteMapTheme {
        Restaurant(Restaurant("test", 4.2, 300))
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    TasteMapTheme {
        HomeScreen(onSignOutClicked = {} )
    }
    
}