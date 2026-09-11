package com.example.critichub.di

import android.content.Context
import com.example.critichub.data.local.db.CriticHubDb
import com.example.critichub.data.local.prefs.UserPreferences
import com.example.critichub.data.repository.AuthRepository
import com.example.critichub.data.repository.CatalogRepository
import com.example.critichub.data.repository.FavoritesRepository
import com.example.critichub.data.repository.MockCatalogRepository

/**
 * ظرف سادهٔ وابستگی‌ها (DI دستی). برای اتصال به API واقعی کافی است
 * [catalogRepository] با پیاده‌سازی شبکهٔ Retrofit جایگزین شود.
 */
class AppContainer(context: Context) {

    private val appContext = context.applicationContext

    val preferences: UserPreferences by lazy { UserPreferences(appContext) }

    val database: CriticHubDb by lazy { CriticHubDb(appContext) }

    val authRepository: AuthRepository by lazy {
        AuthRepository(database, preferences)
    }

    val favoritesRepository: FavoritesRepository by lazy {
        FavoritesRepository(database, preferences)
    }

    val catalogRepository: CatalogRepository by lazy {
        MockCatalogRepository()
    }
}
