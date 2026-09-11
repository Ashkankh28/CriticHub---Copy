package com.example.critichub.model

/** قسمت یک مجموعهٔ تلویزیونی همراه با امتیاز آن. */
data class Episode(
    val number: Int,
    val titleFa: String,
    val titleEn: String = "",
    val rating: Double
)

/** فصل یک مجموعهٔ تلویزیونی شامل فهرست قسمت‌ها. */
data class Season(
    val number: Int,
    val episodes: List<Episode>
)
