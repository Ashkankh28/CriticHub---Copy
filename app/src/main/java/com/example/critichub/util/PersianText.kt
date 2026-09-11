package com.example.critichub.util

import kotlin.math.roundToInt

/**
 * ابزارهای قالب‌بندی متن فارسی؛ تبدیل ارقام لاتین به فارسی و قالب‌های نمایشی.
 */
private val FA_DIGITS = charArrayOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')

/** تبدیل ارقام انگلیسی و ممیز لاتین به ارقام و ممیز فارسی. */
fun String.toFaDigits(): String {
    val sb = StringBuilder(length)
    for (c in this) {
        sb.append(
            when (c) {
                in '0'..'9' -> FA_DIGITS[c - '0']
                '.' -> '٫'
                else -> c
            }
        )
    }
    return sb.toString()
}

fun Int.fa(): String = toString().toFaDigits()

fun Long.fa(): String = toString().toFaDigits()

/** نمایش امتیاز مانند ۸٫۷ یا ۹ */
fun Double.faRating(): String {
    val rounded = (this * 10).roundToInt() / 10.0
    val raw = if (rounded % 1.0 == 0.0) rounded.toInt().toString() else rounded.toString()
    return raw.toFaDigits()
}

/** تعداد رای به‌صورت فشردهٔ فارسی: ۲٫۴ میلیون / ۸۵۰ هزار */
fun Int.faVoteCount(): String = when {
    this >= 1_000_000 -> {
        val v = (this / 1_000_000.0 * 10).roundToInt() / 10.0
        val s = if (v % 1.0 == 0.0) v.toInt().toString() else v.toString()
        "${s.toFaDigits()} میلیون"
    }
    this >= 1_000 -> "${(this / 1_000).toInt().fa()} هزار"
    else -> fa()
}

/** نمایش مدت‌زمان به فارسی: ۲ ساعت و ۲۸ دقیقه */
fun Int.faRuntime(): String {
    val h = this / 60
    val m = this % 60
    return when {
        h == 0 -> "${m.fa()} دقیقه"
        m == 0 -> "${h.fa()} ساعت"
        else -> "${h.fa()} ساعت و ${m.fa()} دقیقه"
    }
}
