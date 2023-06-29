package com.example.tastemap.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

@Composable
fun PasswordTextField(
    password: String,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    @OptIn(ExperimentalMaterial3Api::class)
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text("Enter password") },
        modifier = modifier.widthIn(min = 300.dp),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
    )
}

@Composable
fun EmailTextField(
    email: String,
    onEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    @OptIn(ExperimentalMaterial3Api::class)
    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text("Enter Email") },
        modifier = modifier.widthIn(min = 300.dp)
    )
}

@Composable
fun DropdownList(
    caption: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(16.dp)
            .border(1.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(4.dp))
            .background(MaterialTheme.colorScheme.surface)
            .width(300.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "$caption: ${items[selectedIndex]}",
            modifier = Modifier.clickable { expanded = true }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            items.forEachIndexed { index, s ->
                DropdownMenuItem(text = { Text("$s") }, onClick = {
                    onSelectedChange(index)
                    expanded = false
                })
            }
        }
    }
}

@Composable
fun HyperlinkText(url: String) {
    val context = LocalContext.current
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
            append(url)
            addStringAnnotation(
                tag = "URL",
                annotation = url,
                start = 0,
                end = url.length
            )
        }
    }

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(color = Color.Blue),
        onClick = { offset ->
            annotatedString.getStringAnnotations("URL", offset, offset).firstOrNull()?.let { annotation ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                context.startActivity(intent)
            }
        }
    )
}


//@OptIn(ExperimentalPermissionsApi::class)
//@Composable
//fun PermissionRequest() {
//    val permissionState = rememberPermissionState(Manifest.permission.DYNAMIC_RECEIVER_NOT_EXPORTED_PERMISSION)
//    when {
//        permissionState.hasPermission -> Text("Granted!")
//        permissionState.shouldShowRationale -> PermissionRationaleDialog {
//            permissionState.launchPermissionRequest()
//        }
//        permissionState.permissionRequested -> Text("Denied...")
//        else -> SideEffect {
//            permissionState.launchPermissionRequest()
//        }
//    }
//}