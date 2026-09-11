package com.example.critichub.data.remote

/**
 * قرارداد API آیندهٔ CriticHub.
 *
 * در حال حاضر سرویس پشتیبان وجود ندارد و برنامه با دادهٔ محلی (Mock) کار می‌کند.
 * پس از مشخص شدن سرور واقعی، توابع موردنیاز (دریافت فیلم‌ها/سریال‌های محبوب،
 * جزئیات، نقدها، نظرات و…) در همین اینترفیس تعریف و همراه با DTO های متناظر
 * به Retrofit متصل می‌شوند؛ سپس [com.example.critichub.data.repository.CatalogRepository]
 * پیاده‌سازی شبکه خواهد گرفت.
 */
interface MovieApi
