package com.example.critichub.data.local.db

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import java.util.concurrent.Executors

/** ردیف جدول users — ساختار هم‌نام با Entity ای که بعداً در Room تعریف می‌شود. */
data class UserRow(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String,
    val passwordHash: String,
    val createdAt: Long
)

/** ردیف جدول favorites — snapshot آیتم برای نمایش بدون نیاز به جستجو در کاتالوگ. */
data class FavoriteRow(
    val userId: String,
    val contentId: String,
    val contentType: String,
    val titleFa: String,
    val titleEn: String,
    val year: Int,
    val rating: Double,
    val addedAt: Long
)

/**
 * ذخیره‌سازی محلی ساختاریافته روی SQLite.
 *
 * توضیح: در این محیط امکان افزودن وابستگی Room وجود نداشت، بنابراین لایهٔ پایگاه‌داده با
 * SQLite استاندارد پیاده شده اما طرح جدول‌ها عمداً مشابه Entity های Room نگه داشته شده تا
 * مهاجرت آینده به Room فقط با تعویض بدنهٔ همین کلاس (یا نگاشت به DAO) انجام شود.
 */
class CriticHubDb(context: Context) {

    private val helper = Helper(context.applicationContext)

    // دسترسی‌ها روی یک نخ واحد انجام می‌شوند تا تداخلی پیش نیاید.
    private val dbDispatcher: CoroutineDispatcher =
        Executors.newSingleThreadExecutor().asCoroutineDispatcher()
    private val mutex = Mutex()

    private suspend fun <T> access(block: (SQLiteDatabase) -> T): T =
        withContext(dbDispatcher) {
            mutex.withLock { block(helper.writableDatabase) }
        }

    // ------------------------------------------------------------------ users

    suspend fun insertUser(row: UserRow) = access { db ->
        db.insertOrThrow(
            TABLE_USERS,
            null,
            ContentValues().apply {
                put(COL_ID, row.id)
                put(COL_FULL_NAME, row.fullName)
                put(COL_USERNAME, row.username)
                put(COL_EMAIL, row.email)
                put(COL_PASSWORD_HASH, row.passwordHash)
                put(COL_CREATED_AT, row.createdAt)
            }
        )
    }

    suspend fun findUserById(id: String): UserRow? = access { db ->
        db.query(
            TABLE_USERS,
            null,
            "$COL_ID = ?",
            arrayOf(id),
            null, null, null, "1"
        ).use { c -> if (c.moveToFirst()) c.toUserRow() else null }
    }

    suspend fun findUserByUsername(username: String): UserRow? = access { db ->
        db.query(
            TABLE_USERS,
            null,
            "$COL_USERNAME = ?",
            arrayOf(username.trim()),
            null, null, null, "1"
        ).use { c -> if (c.moveToFirst()) c.toUserRow() else null }
    }

    suspend fun findUserByEmail(email: String): UserRow? = access { db ->
        db.query(
            TABLE_USERS,
            null,
            "$COL_EMAIL = ?",
            arrayOf(email.trim()),
            null, null, null, "1"
        ).use { c -> if (c.moveToFirst()) c.toUserRow() else null }
    }

    suspend fun updateUserProfile(id: String, fullName: String, username: String, email: String) =
        access { db ->
            db.update(
                TABLE_USERS,
                ContentValues().apply {
                    put(COL_FULL_NAME, fullName)
                    put(COL_USERNAME, username.trim())
                    put(COL_EMAIL, email.trim())
                },
                "$COL_ID = ?",
                arrayOf(id)
            )
        }

    suspend fun updateUserPassword(id: String, passwordHash: String) = access { db ->
        db.update(
            TABLE_USERS,
            ContentValues().apply { put(COL_PASSWORD_HASH, passwordHash) },
            "$COL_ID = ?",
            arrayOf(id)
        )
    }

    // ------------------------------------------------------------- favorites

    suspend fun insertFavorite(row: FavoriteRow) = access { db ->
        db.insertWithOnConflict(
            TABLE_FAVORITES,
            null,
            ContentValues().apply {
                put(COL_USER_ID, row.userId)
                put(COL_CONTENT_ID, row.contentId)
                put(COL_CONTENT_TYPE, row.contentType)
                put(COL_TITLE_FA, row.titleFa)
                put(COL_TITLE_EN, row.titleEn)
                put(COL_YEAR, row.year)
                put(COL_RATING, row.rating)
                put(COL_ADDED_AT, row.addedAt)
            },
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    suspend fun deleteFavorite(userId: String, contentId: String) = access { db ->
        db.delete(
            TABLE_FAVORITES,
            "$COL_USER_ID = ? AND $COL_CONTENT_ID = ?",
            arrayOf(userId, contentId)
        )
    }

    suspend fun getFavorites(userId: String): List<FavoriteRow> = access { db ->
        db.query(
            TABLE_FAVORITES,
            null,
            "$COL_USER_ID = ?",
            arrayOf(userId),
            null, null,
            "$COL_ADDED_AT DESC"
        ).use { c ->
            buildList {
                while (c.moveToNext()) add(c.toFavoriteRow())
            }
        }
    }

    // ------------------------------------------------------------------ utils

    private fun Cursor.toUserRow(): UserRow = UserRow(
        id = getString(getColumnIndexOrThrow(COL_ID)),
        fullName = getString(getColumnIndexOrThrow(COL_FULL_NAME)),
        username = getString(getColumnIndexOrThrow(COL_USERNAME)),
        email = getString(getColumnIndexOrThrow(COL_EMAIL)),
        passwordHash = getString(getColumnIndexOrThrow(COL_PASSWORD_HASH)),
        createdAt = getLong(getColumnIndexOrThrow(COL_CREATED_AT))
    )

    private fun Cursor.toFavoriteRow(): FavoriteRow = FavoriteRow(
        userId = getString(getColumnIndexOrThrow(COL_USER_ID)),
        contentId = getString(getColumnIndexOrThrow(COL_CONTENT_ID)),
        contentType = getString(getColumnIndexOrThrow(COL_CONTENT_TYPE)),
        titleFa = getString(getColumnIndexOrThrow(COL_TITLE_FA)),
        titleEn = getString(getColumnIndexOrThrow(COL_TITLE_EN)),
        year = getInt(getColumnIndexOrThrow(COL_YEAR)),
        rating = getDouble(getColumnIndexOrThrow(COL_RATING)),
        addedAt = getLong(getColumnIndexOrThrow(COL_ADDED_AT))
    )

    private class Helper(context: Context) :
        SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(
                """
                CREATE TABLE $TABLE_USERS (
                    $COL_ID TEXT PRIMARY KEY NOT NULL,
                    $COL_FULL_NAME TEXT NOT NULL,
                    $COL_USERNAME TEXT NOT NULL UNIQUE,
                    $COL_EMAIL TEXT NOT NULL UNIQUE,
                    $COL_PASSWORD_HASH TEXT NOT NULL,
                    $COL_CREATED_AT INTEGER NOT NULL
                )
                """.trimIndent()
            )
            db.execSQL(
                """
                CREATE TABLE $TABLE_FAVORITES (
                    $COL_USER_ID TEXT NOT NULL,
                    $COL_CONTENT_ID TEXT NOT NULL,
                    $COL_CONTENT_TYPE TEXT NOT NULL,
                    $COL_TITLE_FA TEXT NOT NULL,
                    $COL_TITLE_EN TEXT NOT NULL,
                    $COL_YEAR INTEGER NOT NULL,
                    $COL_RATING REAL NOT NULL,
                    $COL_ADDED_AT INTEGER NOT NULL,
                    PRIMARY KEY ($COL_USER_ID, $COL_CONTENT_ID)
                )
                """.trimIndent()
            )
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
            db.execSQL("DROP TABLE IF EXISTS $TABLE_USERS")
            onCreate(db)
        }
    }

    companion object {
        private const val DB_NAME = "critichub.db"
        private const val DB_VERSION = 1

        private const val TABLE_USERS = "users"
        private const val TABLE_FAVORITES = "favorites"

        private const val COL_ID = "id"
        private const val COL_FULL_NAME = "full_name"
        private const val COL_USERNAME = "username"
        private const val COL_EMAIL = "email"
        private const val COL_PASSWORD_HASH = "password_hash"
        private const val COL_CREATED_AT = "created_at"

        private const val COL_USER_ID = "user_id"
        private const val COL_CONTENT_ID = "content_id"
        private const val COL_CONTENT_TYPE = "content_type"
        private const val COL_TITLE_FA = "title_fa"
        private const val COL_TITLE_EN = "title_en"
        private const val COL_YEAR = "year"
        private const val COL_RATING = "rating"
        private const val COL_ADDED_AT = "added_at"
    }
}
