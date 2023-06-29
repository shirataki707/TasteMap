package com.example.tastemap.ui.splash

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.tastemap.TasteMapScreen
import com.example.tastemap.ui.components.FullScreenLoading

@Composable
fun SplashScreen(viewModel: SplashViewModel, navController: NavHostController) {

    val isUserLoggedIn by viewModel.userLoggedIn.collectAsState(initial = null)

    // アプリ起動時にユーザのログイン状態を確認し，ホーム画面またはログイン画面に遷移
    LaunchedEffect(isUserLoggedIn) {
        isUserLoggedIn?.let { loggedIn ->
            if (loggedIn) {
                // ログイン状態の場合，ホーム画面に遷移
                navController.navigate(TasteMapScreen.Home.name) {
                    popUpTo(TasteMapScreen.Splash.name) { inclusive = true }
                }
            } else {
                // ログインしていない場合，ログイン画面に遷移
                navController.navigate(TasteMapScreen.SignIn.name) {
                    popUpTo(TasteMapScreen.Splash.name) { inclusive = true }
                }
            }
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        FullScreenLoading()
    }
}
