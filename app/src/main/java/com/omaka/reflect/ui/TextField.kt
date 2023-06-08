package com.omaka.reflect.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.TweenSpec
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction

@Composable
fun TextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    hintText: String = "",
    textStyle: TextStyle = TextStyle.Default,
    onSubmit: () -> Unit = {},
) {
    val coloredTextStyle = textStyle.copy(
        color = textStyle.color.takeOrElse { MaterialTheme.colorScheme.onSurface }
    )
    BasicTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        textStyle = coloredTextStyle,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
        keyboardActions = KeyboardActions(
            onDone = {
                onSubmit()
            }
        ),
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done
        ),
    )
    { innerTextField ->
        AnimatedVisibility(
            value.isEmpty(),
            enter = fadeIn(TweenSpec(100)),
            exit = fadeOut(TweenSpec(100)),
        ) {
            Text(
                hintText, Modifier.alpha(0.38f),
                style = coloredTextStyle
            )
        }
        innerTextField()
    }
}