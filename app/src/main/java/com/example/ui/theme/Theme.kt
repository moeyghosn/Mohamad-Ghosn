package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val HakawatiColorScheme = darkColorScheme(
    primary = HeritageGoldPrimary,
    onPrimary = ArabianMidnight,
    primaryContainer = HeritageGoldDark,
    onPrimaryContainer = HeritageGoldLight,
    secondary = DesertAmber,
    onSecondary = Color.White,
    secondaryContainer = ArabianNightCardElevated,
    onSecondaryContainer = HeritageGoldLight,
    tertiary = OasisEmerald,
    onTertiary = Color.White,
    background = ArabianMidnight,
    onBackground = ParchmentWhite,
    surface = ArabianNightSurface,
    onSurface = ParchmentWhite,
    surfaceVariant = ArabianNightCard,
    onSurfaceVariant = ParchmentMuted,
    outline = ArabianNightBorder,
    outlineVariant = ArabianNightCardElevated
)

@Composable
fun HakawatiTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = HakawatiColorScheme,
        typography = Typography,
        content = content
    )
}
