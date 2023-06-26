package com.example.tastemap

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tastemap.ui.home.HomeScreen
import com.example.tastemap.ui.registration.RegistrationScreen
import com.example.tastemap.ui.signin.SignInScreen
import com.example.tastemap.ui.signin.SignInViewModel

enum class TasteMapScreen {
    Home,
    SignIn,
    Registration
}

@Composable
fun TasteMapApp(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = TasteMapScreen.SignIn.name,
        modifier = Modifier
    ) {
        composable(route = TasteMapScreen.SignIn.name) { backStackEntry ->
            val signInEntry = remember(backStackEntry) {
                navController.getBackStackEntry(TasteMapScreen.SignIn.name)
            }
            val signInViewModel = hiltViewModel<SignInViewModel>(signInEntry)
            SignInScreen(
                signInViewModel,
                onRegisterButtonClicked = { navController.navigate(TasteMapScreen.Registration.name) },
                onSignInButtonClicked = { navController.navigate(TasteMapScreen.Home.name) })
        }

        composable(route = TasteMapScreen.Registration.name) {
            RegistrationScreen(
                onPopBackButtonClicked = { navController.navigateUp() },
                onRegisterButtonClicked = { navController.navigate(TasteMapScreen.Home.name) })
        }

        composable(route = TasteMapScreen.Home.name) {
            HomeScreen()
        }
    }
}