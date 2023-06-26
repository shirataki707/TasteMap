package com.example.tastemap.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.tastemap.ui.signin.SignInScreen

//@ExperimentalComposeUiApi
//@Composable
//fun NavGraph() {
//    val navController = rememberNavController()
//
//    NavHost(navController, startDestination = "home") {
//        Composable("home") { HomeScreen(navController) }
//        Composable("signin") { SignInScreen(navController) }
//        Composable("registrtion") { RegistrationScreen(navController) }
//    }
//}