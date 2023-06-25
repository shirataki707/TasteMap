package com.example.tastemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.tastemap.data.api.hotpepper.HotPepperApiRequest
import com.example.tastemap.data.api.places.PlacesApiDetailRequest
import com.example.tastemap.data.api.places.PlacesApiIdRequest
import com.example.tastemap.ui.theme.TasteMapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainActivityViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasteMapTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        val request = HotPepperApiRequest(
                            lat = 33.652294,
                            lng = 130.672144,
                            range = 5,
                            keyword = "",
                            order = 4,
                            count = 10
                        )
//                        val request = PlacesApiIdRequest(
//                            input = "居酒家 ぐらんま"
//                        )
//                        val request = PlacesApiDetailRequest(
//                            placeId = "ChIJT76cjUV-QTURQvOgzC3xlVk"
//                        )
                        Button(
                            onClick = { viewModel.fetchRestaurants(request, true) },
                            modifier = Modifier.align(Alignment.Center)
                        ) {
                            Text("API call")
                        }
                    }
                }
            }
        }
    }
}




@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TasteMapTheme {

    }
}