package com.example.tastemap.ui.signin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.tastemap.MainActivityViewModel
import com.example.tastemap.ui.components.EmailTextField
import com.example.tastemap.ui.components.PasswordTextField
import com.example.tastemap.ui.theme.TasteMapTheme

@Composable
fun SignInScreen(
    viewModel: SignInViewModel = viewModel(),
    onRegisterButtonClicked: () -> Unit,
    onSignInButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize()) { // Box added here
        Column(
            modifier = Modifier.align(Alignment.Center), // Alignment added here
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            EmailTextField(
                email = email,
                onEmailChange = { email = it },
                modifier = modifier
            )

            PasswordTextField(
                password = password,
                onPasswordChange = { password = it },
                modifier = modifier
            )

            Button(
                onClick = {
                    viewModel.signIn(email, password, onSignInButtonClicked)
                },
                modifier = modifier.widthIn(min = 250.dp)
            ) {
                Text("SignIn")
            }

            Button(
                onClick = onRegisterButtonClicked,
                modifier = modifier.widthIn(min = 250.dp)
            ) {
                Text("Register")
            }

        }
    }
}

@Preview
@Composable
fun SignInScreenPreview() {
    TasteMapTheme {
        SignInScreen(onRegisterButtonClicked = { /*TODO*/ }, onSignInButtonClicked = { /*TODO*/ })
    }
}