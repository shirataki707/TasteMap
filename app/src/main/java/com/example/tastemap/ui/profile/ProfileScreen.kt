package com.example.tastemap.ui.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.tastemap.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileAppBar(
    modifier: Modifier = Modifier,
    navigateUp: () -> Unit
) {

    TopAppBar(
        title = { Text(stringResource(id = R.string.profile)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(id = R.string.back_button)
                )
            }
        },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileViewModel = viewModel(),
    navigateUp: () -> Unit,
    onSignOutClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold (
        topBar = {
            ProfileAppBar(navigateUp = navigateUp)
        }
    ) { innerPadding ->
        Surface(modifier = modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Column(
                modifier = modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // [TODO] 簡易的にユーザ名と好みを表示．見やすくしよう．
                Text("User Name: ${uiState.userName}")
                Text("Reviews Priorities: ${uiState.userPreferences.reviewPriorities}")
                Text("Smoking Priorities: ${uiState.userPreferences.smokingPriorities}")

                // ログアウトボタン
                Button(
                    onClick = {
                        viewModel.signOut()
                        onSignOutClicked()
                    }
                ) {
                    Text(stringResource(id = R.string.sign_out))
                }
            }
        }
    }
}
