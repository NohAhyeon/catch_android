package com.umc.catchandroid.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Purple80,
    secondary = PurpleGrey80,
    tertiary = Pink80
)

private val LightColorScheme = lightColorScheme(
    primary = CatchPrimary,
    secondary = CatchSecondary,
    tertiary = Pink40,
    background = CatchBackground,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = CatchTextTitle,
    onBackground = CatchTextTitle,
    onSurface = CatchTextTitle
)

@Composable
fun CatchTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color는 기기 배경화면 색상에 따라 앱 색이 바뀌어서
    // 디자인 시안과 다르게 보이는 원인이 됨 -> 항상 false로 고정
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}