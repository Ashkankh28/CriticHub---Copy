package com.example.critichub.model

/**
 * شخص (کارگردان، بازیگر، منتقد یا کاربر) با قابلیت نگهداری هم‌زمان
 * نام فارسی و انگلیسی برای پشتیبانی از هر دو زبان.
 */
data class Person(
    val nameFa: String,
    val nameEn: String = ""
)
