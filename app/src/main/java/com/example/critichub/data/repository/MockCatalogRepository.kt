package com.example.critichub.data.repository

import com.example.critichub.data.mock.MockCatalog
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.Genre
import kotlinx.coroutines.delay

/**
 * پیاده‌سازی نمونهٔ [CatalogRepository] روی دادهٔ محلی Mock.
 *
 * تأخیر کوچک در توابع فقط برای شبیه‌سازی رفتاری است که بعداً هنگام اتصال به
 * API واقعی (شبکه) وجود خواهد داشت تا حالت‌های بارگذاری در UI دیده شوند.
 */
class MockCatalogRepository(
    private val responseDelayMillis: Long = 250L
) : CatalogRepository {

    private val allItems: List<CatalogItem> get() = MockCatalog.items

    override suspend fun getHomeSections(): HomeSections {
        delay(responseDelayMillis)
        val items = allItems
        return HomeSections(
            popularMovies = items
                .filter { it.type == ContentType.MOVIE && it.isTrending }
                .sortedByDescending { it.imdbRating }
                .take(10),
            popularSeries = items
                .filter { it.type == ContentType.SERIES && it.isTrending }
                .sortedByDescending { it.imdbRating }
                .take(8),
            newReleases = items
                .sortedByDescending { it.year }
                .take(12),
            genres = MockCatalog.genres
        )
    }

    override suspend fun getItemById(id: String): CatalogItem? {
        delay(responseDelayMillis / 2)
        return allItems.firstOrNull { it.id == id }
    }

    override suspend fun search(
        query: String,
        type: ContentType?,
        minRating: Double?
    ): List<CatalogItem> {
        delay(responseDelayMillis)
        val q = query.trim()
        if (q.isEmpty()) return emptyList()
        val lower = q.lowercase()
        return allItems
            .asSequence()
            .filter { type == null || it.type == type }
            .filter { minRating == null || it.imdbRating >= minRating }
            .filter { item ->
                item.titleFa.contains(lower, ignoreCase = true) ||
                    item.titleEn.contains(lower, ignoreCase = true) ||
                    item.genres.any { g ->
                        g.nameFa.contains(lower, ignoreCase = true) ||
                            g.nameEn.contains(lower, ignoreCase = true)
                    }
            }
            .sortedByDescending { it.imdbRating }
            .toList()
    }

    override suspend fun filter(
        type: ContentType?,
        genreId: String?,
        minRating: Double?,
        sort: SortOption
    ): List<CatalogItem> {
        delay(responseDelayMillis)
        val items = allItems
            .filter { type == null || it.type == type }
            .filter { genreId == null || it.genres.any { g -> g.id == genreId } }
            .filter { minRating == null || it.imdbRating >= minRating }
        return when (sort) {
            SortOption.RATING -> items.sortedByDescending { it.imdbRating }
            SortOption.NEWEST -> items.sortedByDescending { it.year }
        }
    }

    override suspend fun getGenres(): List<Genre> {
        delay(responseDelayMillis / 2)
        return MockCatalog.genres
    }
}
