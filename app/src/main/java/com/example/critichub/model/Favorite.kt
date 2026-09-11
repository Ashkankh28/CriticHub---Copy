package com.example.critichub.model

/** آیتمی که کاربر به علاقه‌مندی‌ها افزوده است (شامل snapshot برای نمایش بدون نیاز به کاتالوگ). */
data class Favorite(
    val contentId: String,
    val type: ContentType,
    val titleFa: String,
    val titleEn: String,
    val year: Int,
    val rating: Double,
    val addedAt: Long
)
