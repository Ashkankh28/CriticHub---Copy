package com.example.critichub.util

/** اعتبارسنجی‌های ساده و قابل تست برای فرم‌ها. */
object Validation {

    private val EMAIL_REGEX =
        Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")

    fun isBlank(value: String): Boolean = value.isBlank()

    fun isValidEmail(value: String): Boolean {
        val v = value.trim()
        return v.isNotEmpty() && EMAIL_REGEX.matches(v)
    }

    fun isValidUsername(value: String): Boolean = value.trim().length >= 3

    fun isValidPassword(value: String): Boolean = value.length >= 6

    fun passwordsMatch(password: String, confirm: String): Boolean = password == confirm
}
