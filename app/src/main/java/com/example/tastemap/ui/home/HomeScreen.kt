package com.example.tastemap.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import com.example.tastemap.R
import com.example.tastemap.TasteMapApp
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.model.Location
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.ui.components.ErrorDialog
import com.example.tastemap.ui.components.FullScreenLoading
import com.example.tastemap.ui.theme.TasteMapTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onSignOutClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

//    LaunchedEffect(Unit) {
//        viewModel.fetchUserDetails()
//    }

    Surface(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text("User Name: ${uiState.userName}")
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
                    viewModel.searchRestaurants(
                        request = dummyRequest,
                        isSortSelected = true
                    )
                }
            ) {
                Text("Search Restaurants")
            }
//            val tmpRestaurants = listOf(Restaurant(name = "test", rating = 3.2, usrReviews = 4))
            RestaurantsList(restaurants = uiState.restaurants)
        }
        when (val event = uiState.event) {
            is HomeUiState.Event.Loading -> {
                FullScreenLoading()
            }
            is HomeUiState.Event.Failure -> {
                ErrorDialog(event.error, viewModel.dismissError)
            }
            else -> {}
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
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Card(modifier = Modifier
        .padding(16.dp)
        .wrapContentSize()
        .clickable {
            coroutineScope.launch {
                // [TODO] ここを現在地の座標にする必要がある
                // 位置情報をアプリが取得できるようにパーミションを与え，現在地を設定
                val start = Location(33.6537617, 130.6729035)
                val destination = restaurantDetail.location
                openGoogleMap(context, start, destination)
            }
        }
    ) {
        RestaurantContent(restaurantDetail)
    }
}

private fun openGoogleMap(context: Context, start: Location, destination: Location) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setClassName(
        "com.google.android.apps.maps",
        "com.google.android.maps.MapsActivity"
    )

    // 移動手段：電車:r, 車:d, 歩き:w
    val transitOptions = listOf("r", "d", "w")

    // 出発地(緯度，経度), 目的地(緯度，経度), 交通手段
    val uri = String.format(
        Locale.US,
        "http://maps.google.com/maps?saddr=%s,%s&daddr=%s,%s&dirflg=%s",
        start.latitude, start.longitude, destination.latitude, destination.longitude, transitOptions[1]
    )
    intent.data = Uri.parse(uri)
    context.startActivity(intent)
}

@Composable
fun RestaurantContent(restaurantDetail: Restaurant) {
    Column {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = "カード画像",
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.7731f), // 344x194
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(16.dp)) {
            Text(
                "${restaurantDetail.name}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                "star: ${restaurantDetail.rating}, reviews: ${restaurantDetail.userReviews}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
fun RestaurantPReview() {
    TasteMapTheme {
//        Restaurant(Restaurant("test", 4.2, 300))
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    TasteMapTheme {
        HomeScreen(onSignOutClicked = {} )
    }
    
}