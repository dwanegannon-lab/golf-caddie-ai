package com.golfcaddieai.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = GolfGreen,
    secondary = GolfGold,
    tertiary = GolfBlue,
    background = GolfDark,
    surface = GolfSurface
)

private val LightColorScheme = lightColorScheme(
    primary = GolfGreen,
    secondary = GolfGold,
    tertiary = GolfBlue,
    background = GolfSand,
    surface = GolfWhite
)

@Composable
fun GolfCaddieAITheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
