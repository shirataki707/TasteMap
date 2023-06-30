package com.example.tastemap.ui.home

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tastemap.R
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.model.Location
import com.example.tastemap.data.model.Restaurant
import com.example.tastemap.ui.components.DropdownList
import com.example.tastemap.ui.components.ErrorDialog
import com.example.tastemap.ui.components.FullScreenLoading
import com.example.tastemap.ui.components.HyperlinkText
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.util.Locale

// [TODO] 位置情報のPermission Requestをしよう

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAppBar(
    modifier: Modifier = Modifier,
    onProfileButtonClicked: () -> Unit
) {
    TopAppBar(
        title = { Text(stringResource(id = R.string.app_name))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        actions = {
            IconButton(onClick = onProfileButtonClicked) {
                Icon(
                    Icons.Filled.AccountCircle,
                    contentDescription = stringResource(id = R.string.profile)
                )
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(),
    onProfileButtonClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    val genres = stringArrayResource(id = R.array.genres).toList()
    val genresCode = stringArrayResource(id = R.array.genresCode).toList()
    val searchRange = stringArrayResource(id = R.array.searchRanges).toList()

    Scaffold(
        topBar = {
            HomeAppBar(onProfileButtonClicked = onProfileButtonClicked)
        }
    ) { innerPadding ->
        Surface(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.space_small))
            ) {

                // [NOTE] なんかうまく検索できない
//                @OptIn(ExperimentalMaterial3Api::class)
//                TextField(
//                    value = uiState.keyword,
//                    onValueChange = { keyword -> viewModel.updateKeyword(keyword) },
//                    label = { Text("検索キーワード (任意)") }
//                )

                DropdownList(
                    caption = stringResource(id = R.string.genre),
                    items = genres,
                    selectedIndex = uiState.genreIndex,
                    onSelectedChange = viewModel.updateGenreIndex,
                )

                DropdownList(
                    caption = stringResource(id = R.string.range),
                    items = searchRange,
                    selectedIndex = uiState.searchRangeIndex,
                    onSelectedChange = viewModel.updateSearchRangeIndex,
                )

                Switch(
                    checked = uiState.isSortOptionSelected,
                    onCheckedChange = { viewModel.updateIsSortOptionChecked(!uiState.isSortOptionSelected) })

                Button(
                    onClick = {
                        // [TODO] 現在地を取得しよう
                        val request = HotPepperApiRequest(
                            lat = 33.652294,
                            lng = 130.672144,
                            range = uiState.searchRangeIndex + 1, // [NOTE] apiのrangeは1から始まるが，uiStateは0から始まるため
                            keyword = uiState.keyword,
                            genre = genresCode[uiState.genreIndex]
                        )

                        viewModel.searchRestaurants(request)
                    }
                ) {
                    Text(stringResource(id = R.string.search_restaurants))
                }

                RestaurantsList(restaurants = uiState.restaurants)
            }

            when (val event = uiState.event) {
                is HomeUiState.Event.Loading -> {
                    FullScreenLoading()
                }
                is HomeUiState.Event.Failure -> {
                    ErrorDialog(stringResource(id = R.string.error), event.error, viewModel.dismissError)
                }
                else -> {}
            }
        }
    }

}

// レストランをスクロール可能なリストで表示
@Composable
fun RestaurantsList(restaurants: List<Restaurant>) {
    LazyColumn {
        items(restaurants) { restaurantDetail ->
            Restaurant(restaurantDetail)
        }
    }
}

// レストラン情報の表示．クリックでGoogleマップを開く
@Composable
fun Restaurant(restaurantDetail: Restaurant) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Card(modifier = Modifier
        .padding(dimensionResource(id = R.dimen.padding_medium))
        .wrapContentSize()
        .clickable {
            coroutineScope.launch {
                // [TODO] ここを現在地の座標にする必要がある
                // 位置情報をアプリが取得できるようにパーミションを与え，現在地を設定
                val start = Location(33.6537617, 130.6729035)
                openGoogleMap(context, start, restaurantDetail.name)
            }
        }
    ) {
        RestaurantContent(restaurantDetail = restaurantDetail)
    }
}

// Googleマップを開く
private fun openGoogleMap(context: Context, start: Location, destination: String) {
    val intent = Intent()
    intent.action = Intent.ACTION_VIEW
    intent.setClassName(
        "com.google.android.apps.maps",
        "com.google.android.maps.MapsActivity"
    )

    // 移動手段：電車:r, 車:d, 歩き:w
    val transitOptions = listOf("r", "d", "w")

    // 出発地(緯度，経度), 目的地(飲食店名), 交通手段
    val uri = String.format(
        Locale.US,
        "http://maps.google.com/maps?saddr=%s,%s&daddr=%s&dirflg=%s",
        start.latitude, start.longitude, URLEncoder.encode(destination, "UTF-8"), transitOptions[1]
    )
    intent.data = Uri.parse(uri)
    context.startActivity(intent)
}

// レストラン情報の中身
@Composable
fun RestaurantContent(
    modifier: Modifier = Modifier,
    restaurantDetail: Restaurant) {
    Column {
        Image(
            painter = painterResource(R.drawable.ic_launcher_foreground),
            contentDescription = stringResource(id = R.string.restaurant),
            modifier = modifier
                .fillMaxWidth()
                .aspectRatio(ratio = 1.7731f), // 344x194
            alignment = Alignment.Center,
            contentScale = ContentScale.Crop
        )
        Column(Modifier.padding(dimensionResource(id = R.dimen.padding_medium))) {
            Text(
                "${restaurantDetail.name}",
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                stringResource(
                    id = R.string.star_reviews, restaurantDetail.rating, restaurantDetail.userReviews
                ),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(id = R.string.price_level, restaurantDetail.priceLevel),
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                stringResource(id = R.string.opening, restaurantDetail.isOpenNow),
                style = MaterialTheme.typography.bodyMedium
            )
            HyperlinkText(url = restaurantDetail.website)
        }
    }
}
