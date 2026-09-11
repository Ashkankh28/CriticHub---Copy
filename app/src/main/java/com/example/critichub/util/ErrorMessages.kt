package com.example.critichub.util

import com.example.critichub.data.repository.AuthError

/** نگاشت خطاهای احراز هویت به پیام فارسی. */
fun AuthError.persianMessage(): String = when (this) {
    AuthError.INVALID_CREDENTIALS -> "نام کاربری یا رمز عبور اشتباه است"
    AuthError.USERNAME_TAKEN -> "این نام کاربری قبلاً استفاده شده است"
    AuthError.EMAIL_TAKEN -> "این ایمیل قبلاً ثبت شده است"
    AuthError.ACCOUNT_NOT_FOUND -> "کاربری با این ایمیل یافت نشد"
    AuthError.GENERIC -> "خطایی رخ داد؛ لطفاً دوباره تلاش کنید"
}
