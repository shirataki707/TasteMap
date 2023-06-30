package com.example.tastemap

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import com.example.tastemap.ui.theme.TasteMapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TasteMapTheme {
                TasteMapApp()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            // Android 12 (APIレベル 31) 以上の場合に実行するコード
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                val animatorSet = AnimatorSet()
                animatorSet.play(
                    ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.SCALE_X,
                        1f,
                        20f,
                    )
                ).with(
                    ObjectAnimator.ofFloat(
                        splashScreenView,
                        View.SCALE_Y,
                        1f,
                        20f,
                    )
                )
                animatorSet.duration = 800L

                animatorSet.doOnEnd { splashScreenView.remove() }

                animatorSet.start()
            }
        } else {
            // Android 11 (APIレベル 30) 以下の場合に実行するコード
        }
    }
}
