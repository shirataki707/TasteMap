package com.example.tastemap

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.tastemap.ui.home.HomeScreen
import com.example.tastemap.ui.home.HomeViewModel
import com.example.tastemap.ui.profile.ProfileScreen
import com.example.tastemap.ui.profile.ProfileViewModel
import com.example.tastemap.ui.registration.RegistrationScreen
import com.example.tastemap.ui.registration.RegistrationViewModel
import com.example.tastemap.ui.signin.SignInScreen
import com.example.tastemap.ui.signin.SignInViewModel
import com.example.tastemap.ui.splash.SplashScreen
import com.example.tastemap.ui.splash.SplashViewModel

enum class TasteMapScreen {
    Home,
    SignIn,
    Registration,
    Splash,
    Profile
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

        // [NOTE] popUpToで引数の画面までスタックから除去．inclusive = trueで引数の画面も含めて除去
        composable(route = TasteMapScreen.SignIn.name) { backStackEntry ->
            val signInViewModel = hiltViewModel<SignInViewModel>(backStackEntry)
            SignInScreen(
                viewModel = signInViewModel,
                onRegisterButtonClicked = { navController.navigate(TasteMapScreen.Registration.name) },
                onSignInButtonClicked = {
                    navController.navigate(TasteMapScreen.Home.name) {
                        popUpTo(TasteMapScreen.SignIn.name) { inclusive = true }
                    }
                }
            )
        }

        composable(route = TasteMapScreen.Registration.name) { backStackEntry ->
            val registrationViewModel = hiltViewModel<RegistrationViewModel>(backStackEntry)
            RegistrationScreen(
                viewModel = registrationViewModel,
                onPopBackButtonClicked = { navController.navigateUp() },
                onRegisterButtonClicked = {
                    navController.navigate(TasteMapScreen.Home.name) {
                        launchSingleTop = true
                        popUpTo(TasteMapScreen.SignIn.name) { inclusive = true }
                    }
                }
            )
        }

        composable(route = TasteMapScreen.Home.name) { backStackEntry ->
            val homeViewModel = hiltViewModel<HomeViewModel>(backStackEntry)
            HomeScreen(
                viewModel = homeViewModel,
                onProfileButtonClicked = {
                    navController.navigate(TasteMapScreen.Profile.name)
                }
            )
        }

        composable(route = TasteMapScreen.Profile.name) { backStackEntry ->
            val profileViewModel = hiltViewModel<ProfileViewModel>(backStackEntry)
            ProfileScreen(
                viewModel = profileViewModel,
                navigateUp = { navController.navigateUp() },
                onSignOutClicked = {
                    navController.navigate(TasteMapScreen.SignIn.name) {
                        popUpTo(TasteMapScreen.Profile.name) {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
