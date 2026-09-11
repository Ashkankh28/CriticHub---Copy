package com.example.critichub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.core.view.WindowCompat
import com.example.critichub.data.local.prefs.ThemeMode
import com.example.critichub.navigation.CriticHubNavHost
import com.example.critichub.ui.theme.CriticHubTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val container = (application as CriticHubApplication).container

        setContent {
            // تم انتخابی کاربر از تنظیمات خوانده می‌شود؛ در حالت پیش‌فرض از تم سیستم پیروی می‌کند.
            val themeMode by container.preferences.themeMode.collectAsState()
            val systemDark = isSystemInDarkTheme()
            val darkTheme = when (themeMode) {
                ThemeMode.LIGHT -> false
                ThemeMode.DARK -> true
                ThemeMode.SYSTEM -> systemDark
            }

            val window = window
            SideEffect {
                WindowCompat.getInsetsController(window, window.decorView).apply {
                    isAppearanceLightStatusBars = !darkTheme
                    isAppearanceLightNavigationBars = !darkTheme
                }
            }

            CriticHubTheme(darkTheme = darkTheme) {
                // کل برنامه راست ‌به‌چپ است.
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    CriticHubNavHost(container)
                }
            }
        }
    }
}
