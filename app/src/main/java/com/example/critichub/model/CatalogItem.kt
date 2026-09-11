package com.example.critichub.model

/**
 * آیتم کاتالوگ: یک مدل یکپارچه برای فیلم (MOVIE) و مجموعهٔ تلویزیونی (SERIES).
 *
 * سریال‌ها فهرست [seasons] دارند؛ فیلم‌ها این فیلد را خالی نگه می‌دارند.
 * نام فارسی و انگلیسی برای عنوان، کارگردان، بازیگران و ژانرها پشتیبانی می‌شود.
 * [posterUrl] در نسخهٔ API آینده پر می‌شود؛ در نمونهٔ فعلی پوستر گرافیکی رسم می‌شود.
 */
data class CatalogItem(
    val id: String,
    val type: ContentType,
    val titleFa: String,
    val titleEn: String,
    val year: Int,
    val yearEnd: Int? = null,
    val runtimeMinutes: Int? = null,
    val genres: List<Genre> = emptyList(),
    val imdbRating: Double,
    val voteCount: Int,
    val synopsis: String,
    val director: Person? = null,
    val cast: List<Person> = emptyList(),
    val criticReviews: List<CriticReview> = emptyList(),
    val userComments: List<UserComment> = emptyList(),
    val seasons: List<Season> = emptyList(),
    val isTrending: Boolean = false,
    val posterUrl: String? = null
) {
    val isSeries: Boolean get() = type == ContentType.SERIES

    val totalEpisodes: Int get() = seasons.sumOf { it.episodes.size }
}
