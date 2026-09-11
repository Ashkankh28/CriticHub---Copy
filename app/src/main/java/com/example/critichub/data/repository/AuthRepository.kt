package com.example.critichub.data.repository

import com.example.critichub.data.local.auth.PasswordHasher
import com.example.critichub.data.local.db.CriticHubDb
import com.example.critichub.data.local.db.UserRow
import com.example.critichub.data.local.prefs.UserPreferences
import com.example.critichub.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.UUID

/**
 * مدیریت احراز هویت محلی: ثبت‌نام، ورود، خروج، تغییر رمز و ویرایش پروفایل.
 * کاربران و هش رمز در پایگاه‌داده محلی ذخیره می‌شوند و نشست در تنظیمات برنامه.
 */
class AuthRepository(
    private val db: CriticHubDb,
    private val prefs: UserPreferences
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _currentUser = MutableStateFlow<User?>(null)

    /** کاربرِ واردشدهٔ فعلی؛ با خروج یا ورود به‌صورت خودکار به‌روزرسانی می‌شود. */
    val currentUser: StateFlow<User?> = _currentUser

    init {
        scope.launch {
            prefs.sessionUserId.collect { id ->
                _currentUser.value = if (id == null) null else db.findUserById(id)?.toDomain()
            }
        }
    }

    fun hasSessionNow(): Boolean = prefs.sessionUserId.value != null

    /** ثبت‌نام کاربر جدید؛ در صورت تکراری بودن نام‌کاربری یا ایمیل خطای مناسب برمی‌گرداند. */
    suspend fun register(
        fullName: String,
        username: String,
        email: String,
        password: String
    ): AuthResult<User> {
        return try {
            if (db.findUserByUsername(username) != null) return AuthResult.Failure(AuthError.USERNAME_TAKEN)
            if (db.findUserByEmail(email) != null) return AuthResult.Failure(AuthError.EMAIL_TAKEN)

            val user = User(
                id = UUID.randomUUID().toString(),
                fullName = fullName.trim(),
                username = username.trim(),
                email = email.trim()
            )
            val hash = PasswordHasher.hash(password.toCharArray())
            db.insertUser(
                UserRow(
                    id = user.id,
                    fullName = user.fullName,
                    username = user.username,
                    email = user.email,
                    passwordHash = hash,
                    createdAt = System.currentTimeMillis()
                )
            )
            prefs.setSessionUserId(user.id)
            AuthResult.Success(user)
        } catch (_: Exception) {
            AuthResult.Failure(AuthError.GENERIC)
        }
    }

    /** ورود با نام‌کاربری و رمز عبور. */
    suspend fun login(username: String, password: String): AuthResult<User> {
        return try {
            val row = db.findUserByUsername(username)
            if (row == null || !PasswordHasher.verify(password.toCharArray(), row.passwordHash)) {
                AuthResult.Failure(AuthError.INVALID_CREDENTIALS)
            } else {
                prefs.setSessionUserId(row.id)
                AuthResult.Success(row.toDomain())
            }
        } catch (_: Exception) {
            AuthResult.Failure(AuthError.GENERIC)
        }
    }

    suspend fun logout() {
        prefs.setSessionUserId(null)
    }

    /**
     * تغییر رمز عبور پس از یافتن حساب با ایمیل.
     * (در نسخهٔ واقعی، تأیید ایمیل و ارسال لینک توسط API پشتیبان انجام می‌شود.)
     */
    /** بررسی وجود حساب با ایمیل (مرحلهٔ اول بازیابی رمز). */
    suspend fun findAccountByEmail(email: String): Boolean =
        db.findUserByEmail(email) != null

    suspend fun changePassword(email: String, newPassword: String): AuthResult<Unit> {
        return try {
            val row = db.findUserByEmail(email) ?: return AuthResult.Failure(AuthError.ACCOUNT_NOT_FOUND)
            val hash = PasswordHasher.hash(newPassword.toCharArray())
            db.updateUserPassword(row.id, hash)
            AuthResult.Success(Unit)
        } catch (_: Exception) {
            AuthResult.Failure(AuthError.GENERIC)
        }
    }

    /** ویرایش نام، نام‌کاربری و ایمیل کاربر واردشده. */
    suspend fun updateProfile(
        userId: String,
        fullName: String,
        username: String,
        email: String
    ): AuthResult<User> {
        return try {
            val self = db.findUserById(userId) ?: return AuthResult.Failure(AuthError.GENERIC)
            db.findUserByUsername(username)?.let { if (it.id != userId) return AuthResult.Failure(AuthError.USERNAME_TAKEN) }
            db.findUserByEmail(email)?.let { if (it.id != userId) return AuthResult.Failure(AuthError.EMAIL_TAKEN) }

            db.updateUserProfile(userId, fullName.trim(), username, email)
            val updated = db.findUserById(userId)?.toDomain()
                ?: return AuthResult.Failure(AuthError.GENERIC)
            _currentUser.value = updated
            AuthResult.Success(updated)
        } catch (_: Exception) {
            AuthResult.Failure(AuthError.GENERIC)
        }
    }

    private fun UserRow.toDomain(): User = User(
        id = id,
        fullName = fullName,
        username = username,
        email = email
    )
}
