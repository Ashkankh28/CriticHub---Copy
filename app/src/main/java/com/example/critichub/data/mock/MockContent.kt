package com.example.critichub.data.mock

import com.example.critichub.model.CriticReview
import com.example.critichub.model.Person
import com.example.critichub.model.UserComment

/**
 * توابع کمکی برای ساخت دادهٔ نمونه با کد کمتر.
 * این داده‌ها فقط جنبهٔ نمایشی دارند و در آینده از API دریافت می‌شوند.
 */
internal fun p(nameFa: String, nameEn: String = "") = Person(nameFa = nameFa, nameEn = nameEn)

internal fun rev(
    authorFa: String,
    authorEn: String,
    headline: String,
    body: String,
    score: Double
) = CriticReview(
    author = Person(nameFa = authorFa, nameEn = authorEn),
    headline = headline,
    body = body,
    score = score
)

internal fun com(
    authorFa: String,
    dateFa: String,
    body: String,
    likes: Int
) = UserComment(
    author = Person(nameFa = authorFa),
    dateFa = dateFa,
    body = body,
    likes = likes
)
