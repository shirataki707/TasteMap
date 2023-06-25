package com.example.tastemap

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.tastemap.data.model.UserPreferences
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
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        val request = HotPepperApiRequest(
                            lat = 33.652294,
                            lng = 130.672144,
                            range = 5,
                            keyword = "",
                            order = 4,
                            count = 10
                        )
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.fetchRestaurants(request, true) },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("API call")
                            }
                        }

                        val email = "test2@gmail.com"
                        val password = "12345678"
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.signUp(email, password) },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("SignUp")
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.signIn(email, password) },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("SignIn")
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.signOut() },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("SignOut")
                            }
                        }

                        val userName = "test"
                        val userPreferences = UserPreferences("french", false)
                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.registerUserDetails(userName, userPreferences) },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("Register User Details")
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Button(
                                onClick = { viewModel.fetchUserDetails() },
                                modifier = Modifier.align(Alignment.Center)
                            ) {
                                Text("Fetch User Details")
                            }
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