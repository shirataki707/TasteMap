package com.example.tastemap.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
fun FullScreenLoading() {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White.copy(alpha = 0.7f))) {
        CircularProgressIndicator(Modifier.align(Alignment.Center))
    }
}

@Composable
fun ErrorDialog(
    errorMessage: String,
    onDismissRequest: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = { Text(text = "Error") },
        text = { Text(text = errorMessage) },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(text = "OK")
            }
        }
    )
}