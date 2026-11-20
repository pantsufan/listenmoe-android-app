package me.echeung.moemoekyun.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import me.echeung.moemoekyun.client.api.Station

internal val ColorOnPrimary = Color(0xFFF0F0F2)
internal val ColorJpopPrimary = Color(0xFFFF015B)
internal val ColorJpopContainer = Color(0xFFF60052)
internal val ColorKpopPrimary = Color(0xFF30A9ED)
internal val ColorKpopContainer = Color(0xFF1587C9)

private val Secondary = Color(0xFF8F92A1)
private val Background = Color.Black
private val Surface = Color(0xFF1E212B)
private val Outline = Color(0xFF383C4A)

private val JpopColorScheme = darkColorScheme(
    primary = ColorJpopPrimary,
    onPrimary = ColorOnPrimary,
    secondary = Secondary,
    onSecondary = Color.White,
    primaryContainer = ColorJpopContainer,
    secondaryContainer = ColorJpopContainer,
    background = Background,
    onBackground = Color.White,
    surface = Surface,
    onSurface = Color.White,
    surfaceContainer = Surface,
    onSurfaceVariant = Color(0xFFB0B3C0),
    outline = Outline,
    outlineVariant = Outline,
)

private val KpopColorScheme = darkColorScheme(
    primary = ColorKpopPrimary,
    onPrimary = ColorOnPrimary,
    secondary = Secondary,
    onSecondary = Color.White,
    primaryContainer = ColorKpopContainer,
    secondaryContainer = ColorKpopContainer,
    background = Background,
    onBackground = Color.White,
    surface = Surface,
    onSurface = Color.White,
    surfaceContainer = Surface,
    onSurfaceVariant = Color(0xFFB0B3C0),
    outline = Outline,
    outlineVariant = Outline,
)

private fun colorSchemeForStation(station: Station) = when (station) {
    Station.JPOP -> JpopColorScheme
    Station.KPOP -> KpopColorScheme
}

@Composable
fun AppTheme(station: Station = Station.JPOP, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = colorSchemeForStation(station),
        content = content,
    )
}
