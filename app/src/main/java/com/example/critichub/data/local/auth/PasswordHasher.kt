package com.example.critichub.data.local.auth

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

/**
 * هش امن رمز عبور با PBKDF2 (HMAC-SHA256) و salt تصادفی.
 *
 * قالب ذخیره‌سازی: `iterationCount$saltBase64$hashBase64`
 * رمز عبور هرگز به‌صورت متن ساده ذخیره یا ثبت نمی‌شود.
 */
object PasswordHasher {

    private const val ITERATIONS = 120_000
    private const val KEY_LENGTH_BITS = 256
    private const val SALT_BYTES = 16
    private const val ALGORITHM = "PBKDF2WithHmacSHA256"

    private val random = SecureRandom()

    suspend fun hash(password: CharArray): String = withContext(Dispatchers.Default) {
        val salt = ByteArray(SALT_BYTES).also(random::nextBytes)
        val derived = pbkdf2(password, salt, ITERATIONS)
        val encoder = Base64.getEncoder()
        "$ITERATIONS$${encoder.encodeToString(salt)}$${encoder.encodeToString(derived)}"
    }

    suspend fun verify(password: CharArray, stored: String): Boolean =
        withContext(Dispatchers.Default) {
            try {
                val parts = stored.split('$')
                if (parts.size != 3) return@withContext false
                val iterations = parts[0].toIntOrNull() ?: return@withContext false
                val decoder = Base64.getDecoder()
                val salt = decoder.decode(parts[1])
                val expected = decoder.decode(parts[2])
                val actual = pbkdf2(password, salt, iterations)
                MessageDigest.isEqual(expected, actual)
            } catch (_: Exception) {
                false
            }
        }

    private fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, KEY_LENGTH_BITS)
        try {
            val factory = SecretKeyFactory.getInstance(ALGORITHM)
            return factory.generateSecret(spec).encoded
        } finally {
            spec.clearPassword()
        }
    }
}
