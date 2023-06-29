package com.example.tastemap.ui.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tastemap.ui.components.DropdownList
import com.example.tastemap.ui.components.EmailTextField
import com.example.tastemap.ui.components.ErrorDialog
import com.example.tastemap.ui.components.FullScreenLoading
import com.example.tastemap.ui.components.PasswordTextField
import com.example.tastemap.ui.theme.TasteMapTheme

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel = viewModel(),
    onPopBackButtonClicked: () -> Unit,
    onRegisterButtonClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
//    var userName by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var isSmoker by remember { mutableStateOf(false) }

    val uiState by viewModel.uiState.collectAsState()

    Surface(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                @OptIn(ExperimentalMaterial3Api::class)
                (TextField(
                    value = uiState.userName,
                    onValueChange = { userName -> viewModel.updateUserName(userName) },
                    label = { Text("Enter User Name") },
                    modifier = modifier.widthIn(min = 300.dp)
                ))

                EmailTextField(
                    email = uiState.email,
                    onEmailChange = { email -> viewModel.updateEmail(email) },
                    modifier = modifier,
                    enabled = true
                )

                PasswordTextField(
                    password = uiState.password,
                    onPasswordChange = { password -> viewModel.updatePassword(password) },
                    modifier = modifier,
                    enabled = true
                )
//                Row(
//                    modifier = modifier.width(300.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Text("喫煙席のみ検索")
//                    Spacer(modifier = Modifier.width(16.dp))
//                    Switch(
//                        checkState = uiState.isSmoker,
//                        onChanged = { viewModel.updateIsSmoker(!uiState.isSmoker) },
//                        modifier = modifier
//                    )
//                }
                DropdownList(
                    items = viewModel.reviewPriorities,
                    selectedIndex = uiState.reviewsPrioritiesIndex,
                    onSelectedChange = viewModel.updateReviewPrioritiesIndex,
                    caption = "飲食店の評価方法"
                )

                DropdownList(
                    items = viewModel.smokingPriorities,
                    selectedIndex = uiState.smokingPrioritiesIndex,
                    onSelectedChange = viewModel.updateSmokingPrioritiesIndex,
                    caption = "喫煙・禁煙席の好み"
                )

                Button(
                    onClick = { viewModel.registerAccount(
                        onRegisterSuccess = onRegisterButtonClicked
                    )},
                    modifier = modifier.widthIn(min = 300.dp),
                    enabled = uiState.isSignUpButtonEnabled
                ) {
                    Text("Register")
                }

                Button(
                    onClick = onPopBackButtonClicked,
                    modifier = modifier.widthIn(min = 300.dp)
                ) {
                    Text("To SignIn")
                }

            }
            when (val event = uiState.event) {
                is RegistrationUiState.Event.Loading -> {
                    FullScreenLoading()
                }

                is RegistrationUiState.Event.Failure -> {
                    ErrorDialog("認証エラー", event.error, viewModel.dismissError)
                }

                else -> {}
            }
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