package com.example.critichub.model

/**
 * نوع محتوا: فیلم یا مجموعهٔ تلویزیونی.
 * [storage] مقدار ذخیره‌شده در پایگاه‌داده است.
 */
enum class ContentType(val storage: String) {
    MOVIE("movie"),
    SERIES("series");

    companion object {
        fun fromStorage(value: String?): ContentType? =
            entries.firstOrNull { it.storage == value }
    }
}
