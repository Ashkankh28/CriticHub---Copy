package com.example.critichub.model

/**
 * نظر/دیدگاه کاربران — صرفاً خواندنی؛ کاربران اجازهٔ ثبت، ویرایش یا حذف
 * نظر ندارند و این داده‌ها در آینده از API دریافت می‌شوند.
 */
data class UserComment(
    val author: Person,
    val dateFa: String,
    val body: String,
    val likes: Int
)
