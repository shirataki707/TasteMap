package com.example.tastemap.ui.splash

import androidx.compose.foundation.layout.Arrangement.Absolute.Center
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.tastemap.TasteMapScreen
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(viewModel: SplashViewModel, navController: NavHostController) {
    var isUserLoggedIn by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = viewModel) {
        delay(1000) // delay for 3 seconds
        val loggedIn = viewModel.isUserLoggedIn()
        isUserLoggedIn = loggedIn
        if (loggedIn) {
            navController.navigate(TasteMapScreen.Home.name) {
                popUpTo(TasteMapScreen.Splash.name) { inclusive = true }
            }
        } else {
            navController.navigate(TasteMapScreen.SignIn.name) {
                popUpTo(TasteMapScreen.Splash.name) { inclusive = true }
            }
        }
    }

    // TODO: Display your splash screen here
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Hello TasteMap!!")
    }
}