package com.example.critichub.data.mock

import com.example.critichub.model.Genre

/** ژانرهای استفاده‌شده در دادهٔ نمونه. */
object MockGenres {
    val ACTION = Genre("action", "اکشن", "Action")
    val ADVENTURE = Genre("adventure", "ماجراجویی", "Adventure")
    val ANIMATION = Genre("animation", "انیمیشن", "Animation")
    val BIOGRAPHY = Genre("biography", "زندگی‌نامه‌ای", "Biography")
    val COMEDY = Genre("comedy", "کمدی", "Comedy")
    val CRIME = Genre("crime", "جنایی", "Crime")
    val DRAMA = Genre("drama", "درام", "Drama")
    val FAMILY = Genre("family", "خانوادگی", "Family")
    val FANTASY = Genre("fantasy", "فانتزی", "Fantasy")
    val HISTORY = Genre("history", "تاریخی", "History")
    val HORROR = Genre("horror", "ترسناک", "Horror")
    val MYSTERY = Genre("mystery", "معمایی", "Mystery")
    val ROMANCE = Genre("romance", "عاشقانه", "Romance")
    val SCIFI = Genre("scifi", "علمی-تخیلی", "Sci-Fi")
    val THRILLER = Genre("thriller", "هیجان‌انگیز", "Thriller")
    val WAR = Genre("war", "جنگی", "War")

    val ALL: List<Genre> = listOf(
        ACTION, ADVENTURE, ANIMATION, BIOGRAPHY, COMEDY, CRIME, DRAMA,
        FAMILY, FANTASY, HISTORY, HORROR, MYSTERY, ROMANCE, SCIFI,
        THRILLER, WAR
    )

    fun byId(id: String?): Genre? = ALL.firstOrNull { it.id == id }
}
