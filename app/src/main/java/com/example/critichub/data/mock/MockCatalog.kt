package com.example.critichub.data.mock

import com.example.critichub.model.CatalogItem
import com.example.critichub.model.Genre

/** مجموعهٔ کامل دادهٔ نمونه. */
object MockCatalog {
    val items: List<CatalogItem> by lazy { MockMovies.all + MockSeries.all }
    val genres: List<Genre> get() = MockGenres.ALL
}
