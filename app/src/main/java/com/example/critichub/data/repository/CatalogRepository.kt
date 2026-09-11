package com.example.critichub.data.repository

import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.Genre

/** گزینه‌های مرتب‌سازی نتایج فیلتر/جستجو. */
enum class SortOption { RATING, NEWEST }

/** بخش‌های صفحهٔ اصلی. */
data class HomeSections(
    val popularMovies: List<CatalogItem>,
    val popularSeries: List<CatalogItem>,
    val newReleases: List<CatalogItem>,
    val genres: List<Genre> = emptyList()
)

/**
 * قرارداد دسترسی به کاتالوگ فیلم‌ها و سریال‌ها.
 *
 * پیاده‌سازی فعلی نمونه (Mock) است؛ برای اتصال به API واقعی کافی است یک
 * پیاده‌سازی جدید بر پایهٔ Retrofit نوشته و در [com.example.critichub.di.AppContainer]
 * جایگزین شود. تمام توابع تعلیقی هستند تا با شبکه سازگار بمانند.
 */
interface CatalogRepository {

    suspend fun getHomeSections(): HomeSections

    suspend fun getItemById(id: String): CatalogItem?

    /** جستجوی جزئی در عنوان فارسی/انگلیسی با فیلتر نوع و حداقل امتیاز. */
    suspend fun search(
        query: String,
        type: ContentType?,
        minRating: Double?
    ): List<CatalogItem>

    /** فیلتر بر اساس نوع، ژانر، حداقل امتیاز و مرتب‌سازی. */
    suspend fun filter(
        type: ContentType?,
        genreId: String?,
        minRating: Double?,
        sort: SortOption
    ): List<CatalogItem>

    suspend fun getGenres(): List<Genre>
}
