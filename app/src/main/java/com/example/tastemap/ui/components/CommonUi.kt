package com.example.tastemap.ui.components

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.tastemap.R

// パスワード入力欄
@Composable
fun PasswordTextField(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChange: (String) -> Unit,
    enabled: Boolean
) {
    @OptIn(ExperimentalMaterial3Api::class)
    TextField(
        value = password,
        onValueChange = onPasswordChange,
        label = { Text(stringResource(id = R.string.enter_password)) },
        modifier = modifier.width(dimensionResource(id = R.dimen.width_medium)),
        visualTransformation = PasswordVisualTransformation(),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        enabled = enabled,
        maxLines = 1,
        singleLine = true
    )
}

// メールアドレス入力欄
@Composable
fun EmailTextField(
    modifier: Modifier = Modifier,
    email: String,
    onEmailChange: (String) -> Unit,
    enabled: Boolean
) {
    @OptIn(ExperimentalMaterial3Api::class)
    TextField(
        value = email,
        onValueChange = onEmailChange,
        label = { Text(stringResource(id = R.string.enter_email)) },
        modifier = modifier.width(dimensionResource(id = R.dimen.width_medium)),
        enabled = enabled,
        maxLines = 1,
        singleLine = true
    )
}

// ドロップダウンリスト
@Composable
fun DropdownList(
    modifier: Modifier = Modifier,
    caption: String,
    items: List<String>,
    selectedIndex: Int,
    onSelectedChange: (Int) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .padding(dimensionResource(id = R.dimen.padding_medium))
            .border(
                dimensionResource(id = R.dimen.border_small),
                MaterialTheme.colorScheme.onSurface, RoundedCornerShape(
                    dimensionResource(
                        id = R.dimen.corner_small
                    )
                )
            )
            .background(MaterialTheme.colorScheme.surface)
            .width(dimensionResource(id = R.dimen.width_medium)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(
                id = R.string.dropdown_text,
                "$caption",
                "${items[selectedIndex]}"
            ),
            modifier = modifier.clickable { expanded = true }
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

// ハイパーリンクのテキスト
@Composable
fun HyperlinkText(url: String) {
    val context = LocalContext.current
    val tag = stringResource(id = R.string.url)
    val annotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(textDecoration = TextDecoration.LineThrough)) {
            append(url)
            addStringAnnotation(
                tag = tag,
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
            annotatedString.getStringAnnotations("$tag", offset, offset).firstOrNull()?.let { annotation ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                context.startActivity(intent)
            }
        }
    )
}
