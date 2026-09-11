package com.example.critichub.data.repository

import com.example.critichub.data.local.db.CriticHubDb
import com.example.critichub.data.local.db.FavoriteRow
import com.example.critichub.data.local.prefs.UserPreferences
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.Favorite
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

/**
 * مدیریت علاقه‌مندی‌ها. آیتم‌ها با snapshot در پایگاه‌داده محلی ذخیره و
 * پس از ورود/خروج کاربر به‌صورت خودکار بارگذاری می‌شوند.
 */
class FavoritesRepository(
    private val db: CriticHubDb,
    private val prefs: UserPreferences
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _favorites = MutableStateFlow<List<Favorite>>(emptyList())
    val favorites: StateFlow<List<Favorite>> = _favorites

    init {
        scope.launch {
            prefs.sessionUserId.collect { id ->
                _favorites.value = if (id == null) emptyList() else loadFavorites(id)
            }
        }
    }

    /** افزودن یا حذف یک آیتم از علاقه‌مندی‌های کاربرِ واردشده. */
    suspend fun toggle(item: CatalogItem) {
        val userId = prefs.sessionUserId.value ?: return
        val exists = _favorites.value.any { it.contentId == item.id }
        if (exists) {
            db.deleteFavorite(userId, item.id)
        } else {
            db.insertFavorite(
                FavoriteRow(
                    userId = userId,
                    contentId = item.id,
                    contentType = item.type.storage,
                    titleFa = item.titleFa,
                    titleEn = item.titleEn,
                    year = item.year,
                    rating = item.imdbRating,
                    addedAt = System.currentTimeMillis()
                )
            )
        }
        _favorites.value = loadFavorites(userId)
    }

    /** حذف آیتم از علاقه‌مندی‌ها (فقط با شناسه). */
    suspend fun remove(contentId: String) {
        val userId = prefs.sessionUserId.value ?: return
        db.deleteFavorite(userId, contentId)
        _favorites.value = loadFavorites(userId)
    }

    private suspend fun loadFavorites(userId: String): List<Favorite> =
        db.getFavorites(userId).map { row ->
            Favorite(
                contentId = row.contentId,
                type = ContentType.fromStorage(row.contentType) ?: ContentType.MOVIE,
                titleFa = row.titleFa,
                titleEn = row.titleEn,
                year = row.year,
                rating = row.rating,
                addedAt = row.addedAt
            )
        }
}
