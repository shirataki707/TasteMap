package com.example.tastemap

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tastemap.ui.home.HomeScreen
import com.example.tastemap.ui.home.HomeViewModel
import com.example.tastemap.ui.registration.RegistrationScreen
import com.example.tastemap.ui.signin.SignInScreen
import com.example.tastemap.ui.signin.SignInViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tastemap.ui.splash.SplashScreen
import com.example.tastemap.ui.splash.SplashViewModel

enum class TasteMapScreen {
    Home,
    SignIn,
    Registration,
    Splash
}

@Composable
fun TasteMapApp(
    navController: NavHostController = rememberNavController()
) {

    NavHost(
        navController = navController,
        startDestination = TasteMapScreen.Splash.name,
        modifier = Modifier
    ) {
        composable(route = TasteMapScreen.Splash.name) { backStackEntry ->
            val splashViewModel = hiltViewModel<SplashViewModel>(backStackEntry)
            SplashScreen(viewModel = splashViewModel, navController = navController)
        }

        composable(route = TasteMapScreen.SignIn.name) { backStackEntry ->
            // [TODO] よくわからんから調べよう
//            val signInEntry = remember(backStackEntry) {
//                navController.getBackStackEntry(TasteMapScreen.SignIn.name)
//            }
            val signInViewModel = hiltViewModel<SignInViewModel>(backStackEntry)
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

        composable(route = TasteMapScreen.Home.name) { backStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>(backStackEntry)
            // [TODO] ただの遷移だとスタックに残りそう
            HomeScreen(
                homeViewModel,
                onSignOutClicked = { navController.navigate(TasteMapScreen.SignIn.name) }
            )
        }
    }
}