package com.example.critichub.data.local.prefs

import android.content.Context
import android.content.SharedPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

/**
 * تنظیمات سبک برنامه (نشست کاربر و تم) با SharedPreferences.
 *
 * در نسخهٔ نهایی می‌توان این کلاس را با پیاده‌سازی DataStore جایگزین کرد؛
 * واسط استفاده‌کننده (StateFlow + توابع تعلیقی) بدون تغییر باقی می‌ماند.
 */
class UserPreferences(context: Context) {

    private val prefs: SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _sessionUserId = MutableStateFlow(prefs.getString(KEY_SESSION_USER_ID, null))
    val sessionUserId: StateFlow<String?> = _sessionUserId

    private val _themeMode = MutableStateFlow(readThemeMode())
    val themeMode: StateFlow<ThemeMode> = _themeMode

    init {
        prefs.registerOnSharedPreferenceChangeListener { _, key ->
            when (key) {
                KEY_SESSION_USER_ID ->
                    _sessionUserId.value = prefs.getString(KEY_SESSION_USER_ID, null)
                KEY_THEME_MODE -> _themeMode.value = readThemeMode()
            }
        }
    }

    private fun readThemeMode(): ThemeMode = when (prefs.getString(KEY_THEME_MODE, null)) {
        "light" -> ThemeMode.LIGHT
        "dark" -> ThemeMode.DARK
        else -> ThemeMode.SYSTEM
    }

    suspend fun setSessionUserId(id: String?) {
        withContext(Dispatchers.IO) {
            prefs.edit().putString(KEY_SESSION_USER_ID, id).commit()
        }
        _sessionUserId.value = id
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        withContext(Dispatchers.IO) {
            when (mode) {
                ThemeMode.SYSTEM -> prefs.edit().remove(KEY_THEME_MODE)
                ThemeMode.LIGHT -> prefs.edit().putString(KEY_THEME_MODE, "light")
                ThemeMode.DARK -> prefs.edit().putString(KEY_THEME_MODE, "dark")
            }.commit()
        }
        _themeMode.value = mode
    }

    companion object {
        private const val PREFS_NAME = "critichub_prefs"
        private const val KEY_SESSION_USER_ID = "session_user_id"
        private const val KEY_THEME_MODE = "theme_mode"
    }
}
