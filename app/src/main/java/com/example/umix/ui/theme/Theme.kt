package com.example.umix.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val UmixColorScheme = lightColorScheme(
    primary = UmixNavy,
    onPrimary = UmixWhite,
    secondary = UmixYellow,
    onSecondary = UmixInk,
    tertiary = UmixBlue,
    background = UmixCream,
    onBackground = UmixInk,
    surface = UmixCream,
    onSurface = UmixInk,
    error = UmixRed,
)

@Composable
fun UmixTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = UmixColorScheme,
        typography = Typography,
        content = content
    )
}
