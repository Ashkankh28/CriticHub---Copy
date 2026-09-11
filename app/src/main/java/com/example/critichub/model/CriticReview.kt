package com.example.critichub.model

/**
 * نقد منتقدان — صرفاً خواندنی. در نسخهٔ نهایی این داده‌ها از API دریافت می‌شود.
 */
data class CriticReview(
    val author: Person,
    val headline: String,
    val body: String,
    val score: Double
)
