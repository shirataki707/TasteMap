package com.example.tastemap.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tastemap.ui.theme.TasteMapTheme

@Composable
fun RegistrationScreen(
    onPopBackButtonClicked: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onRegisterButtonClicked,
            modifier = modifier.widthIn(min = 250.dp)
        ) {
            Text("Register")
        }

        Button(
            onClick = onPopBackButtonClicked,
            modifier = modifier.widthIn(min = 250.dp)
        ) {
            Text("To SignIn")
        }

    }
}

@Preview
@Composable
fun RegistrationScreenPreview() {
    TasteMapTheme {
        RegistrationScreen(
            onPopBackButtonClicked = { /*TODO*/ },
            onRegisterButtonClicked = { /*TODO*/ })
    }
}