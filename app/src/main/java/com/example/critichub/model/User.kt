package com.example.critichub.model

/**
 * کاربر برنامه (بدون اطلاعات حساس مانند هش رمز؛ آن‌ها فقط در لایهٔ داده نگهداری می‌شوند).
 */
data class User(
    val id: String,
    val fullName: String,
    val username: String,
    val email: String
)
