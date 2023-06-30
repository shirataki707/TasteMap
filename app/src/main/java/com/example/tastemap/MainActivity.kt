package com.example.tastemap

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.tastemap.ui.theme.TasteMapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

        setContent {
            TasteMapTheme {
                TasteMapApp()
            }
        }
    }
}
