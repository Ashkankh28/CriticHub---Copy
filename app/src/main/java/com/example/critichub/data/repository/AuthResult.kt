package com.example.critichub.data.repository

/** خطاهای سطح احراز هویت؛ UI وظیفهٔ نگاشت آن‌ها به پیام فارسی را دارد. */
enum class AuthError {
    USERNAME_TAKEN,
    EMAIL_TAKEN,
    INVALID_CREDENTIALS,
    ACCOUNT_NOT_FOUND,
    GENERIC
}

sealed interface AuthResult<out T> {
    data class Success<T>(val data: T) : AuthResult<T>
    data class Failure(val error: AuthError) : AuthResult<Nothing>
}
