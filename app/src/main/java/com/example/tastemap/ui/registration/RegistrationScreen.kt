package com.example.tastemap.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tastemap.ui.components.EmailTextField
import com.example.tastemap.ui.components.PasswordTextField
import com.example.tastemap.ui.theme.TasteMapTheme
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = viewModel(),
    onPopBackButtonClicked: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    var userName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isSmoker by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            @OptIn(ExperimentalMaterial3Api::class)
            (TextField(
                value = userName,
                onValueChange = { userName = it },
                label = { Text("Enter User Name") },
                modifier = modifier.widthIn(min = 250.dp)
            ))

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
            Row(
                modifier = modifier.width(250.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Are you a Smoker?")
                Spacer(modifier = Modifier.width(16.dp))
                Switch(
                    checkState = isSmoker,
                    onChanged = { isSmoker = !isSmoker },
                    modifier = modifier
                )
            }


            Button(
                onClick = { viewModel.RegisterAccount(
                    userName = userName,
                    email = email,
                    password = password,
                    isSmoker = isSmoker,
                    onRegisterSuccess = onRegisterButtonClicked
                )},
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
}

@Composable
fun Switch(
    checkState: Boolean,
    onChanged: () -> Unit,
    modifier: Modifier
) {
    Switch(
        checked = checkState,
        onCheckedChange = { onChanged() }
    )
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