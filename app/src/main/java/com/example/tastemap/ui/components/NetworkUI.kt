package com.example.tastemap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.tastemap.R
import kotlinx.coroutines.delay

// ローディング画面
@Composable
fun FullScreenLoading(
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier
        .fillMaxSize()
        .background(Color.White.copy(alpha = 0.7f))) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Composable
fun FullScreenAnimationLoading() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.loading))
    val progress by animateLottieCompositionAsState(
        composition,
        isPlaying = true,
        iterations = LottieConstants.IterateForever
    )

    LottieAnimation(
        composition = composition,
        progress = progress
    )
}

// エラーダイアログ
@Composable
fun ErrorDialog(
    titleMessage: String,
    errorMessage: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = titleMessage) },
        text = { Text(text = errorMessage) },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = stringResource(id = R.string.ok))
            }
        }
    )
}

// ログインや登録成功時の画面
@Composable
fun SuccessIcon(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Filled.CheckCircle,
                contentDescription = stringResource(id = R.string.signin_success),
                tint = MaterialTheme.colorScheme.secondary,
                modifier = modifier.size(dimensionResource(id = R.dimen.size_medium))
            )

        }
    }
}

//@Composable
//fun SuccessAnimation() {
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
//    val progress by animateLottieCompositionAsState(composition)
//
//    LottieAnimation(
//        composition = composition,
//        progress = progress
//    )
//}
@Composable
fun SuccessAnimation() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.success))
    val progress by animateLottieCompositionAsState(composition, isPlaying = true)

    LaunchedEffect(Unit) {
        delay(2000)
    }

    LottieAnimation(
        composition = composition,
        progress = progress
    )
}



