package com.example.critichub.model

/** ژانر با نام فارسی و انگلیسی. */
data class Genre(
    val id: String,
    val nameFa: String,
    val nameEn: String = ""
)
