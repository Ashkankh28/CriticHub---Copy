package com.example.critichub.navigation

/** ثابت‌های مسیرهای نویگیشن. */
object Routes {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val REGISTER = "register"
    const val CHANGE_PASSWORD = "change_password"
    const val MAIN = "main"
    const val DETAIL = "detail/{contentId}"
    const val BROWSE = "browse?genreId={genreId}&type={type}"
    const val SETTINGS = "settings"
    const val EDIT_PROFILE = "edit_profile"

    // تب‌های ناحیهٔ اصلی
    const val HOME = "home"
    const val SEARCH = "search"
    const val FAVORITES = "favorites"
    const val PROFILE = "profile"

    fun detail(contentId: String) = "detail/$contentId"

    fun browse(genreId: String?, type: com.example.critichub.model.ContentType?): String {
        val genre = genreId ?: "all"
        val typeName = type?.name?.lowercase() ?: "all"
        return "browse?genreId=$genre&type=$typeName"
    }
}
