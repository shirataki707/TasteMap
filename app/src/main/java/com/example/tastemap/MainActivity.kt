package com.example.tastemap

import android.Manifest
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.splashscreen.SplashScreenViewProvider
import com.example.tastemap.ui.theme.TasteMapTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 位置情報のパーミッション確認
        confirmPermission()

        // [NOTE] Android11でデフォルトのTopBarを非表示にする，もっと良いやり方ありそう
        theme.applyStyle(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                R.style.Theme_TasteMap
            } else {
                com.airbnb.lottie.R.style.Theme_AppCompat_Light_NoActionBar
            },
            true
        )

        // Android12以上なら独自のSplash画面を表示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val splashScreen = installSplashScreen()

            splashScreen.setOnExitAnimationListener { splashScreenViewProvider ->
                startSplashAnimation(splashScreenViewProvider)
            }
        }

        setContent {
            TasteMapTheme {
                TasteMapApp()
            }
        }
    }

    // パーミッションを確認して，ない場合はリクエスト
    private fun confirmPermission() {
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))
    }

    // Splashのアニメーション
    private fun startSplashAnimation(splashScreenViewProvider: SplashScreenViewProvider) {
        val splashScreenView = splashScreenViewProvider.iconView

        // アイコンを回転
        val rotationAnimator = ObjectAnimator.ofFloat(splashScreenView, View.ROTATION, 0f, 360f).apply {
            duration = 800L
        }

        // スケール変更
        val scaleAnimatorX= ObjectAnimator.ofFloat(splashScreenView, View.SCALE_X, 1f, 0f).apply {
            duration = 200L
        }
        val scaleAnimatorY = ObjectAnimator.ofFloat(splashScreenView, View.SCALE_Y, 1f, 0f).apply {
            duration = 200L
        }

        // フェードアウト
        val fadeOutAnimator = ObjectAnimator.ofFloat(splashScreenView, View.ALPHA, 1f, 0f).apply {
            duration = 200L
            interpolator = AccelerateInterpolator()
            doOnEnd { splashScreenViewProvider.remove() }
        }

        // run the animations in sequence
        AnimatorSet().apply {
            playSequentially(rotationAnimator, AnimatorSet().apply {
                playTogether(scaleAnimatorX, scaleAnimatorY, fadeOutAnimator)
            })
            start()
        }
    }

}
