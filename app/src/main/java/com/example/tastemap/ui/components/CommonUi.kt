package com.example.tastemap.ui.components

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier
) {
    @OptIn(ExperimentalMaterial3Api::class)
    (TextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Enter password") },
        modifier = modifier.widthIn(min = 250.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    ))
}

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier
) {
    @OptIn(ExperimentalMaterial3Api::class)
    (TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Enter Email") },
        modifier = modifier.widthIn(min = 250.dp)
    ))
}