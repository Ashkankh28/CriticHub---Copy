package com.example.critichub

import com.example.critichub.data.local.auth.PasswordHasher
import com.example.critichub.util.Validation
import com.example.critichub.util.faRating
import com.example.critichub.util.faRuntime
import com.example.critichub.util.faVoteCount
import com.example.critichub.util.toFaDigits
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CriticHubUnitTest {

    @Test
    fun faDigits_convertsLatinDigits() {
        assertEquals("۱۳۹۸", "1398".toFaDigits())
        assertEquals("۸٫۷", "8.7".toFaDigits())
        assertEquals("۰", "0".toFaDigits())
    }

    @Test
    fun faRating_roundsAndConverts() {
        assertEquals("۸٫۷", 8.7.faRating())
        assertEquals("۹", 9.0.faRating())
        assertEquals("۸٫۴", 8.44.faRating())
        assertEquals("۸٫۵", 8.45.faRating())
    }

    @Test
    fun faVoteCount_compactsInPersian() {
        assertEquals("۲٫۴ میلیون", 2_400_000.faVoteCount())
        assertEquals("۸۵۰ هزار", 850_000.faVoteCount())
        assertEquals("۱۲۳", 123.faVoteCount())
    }

    @Test
    fun faRuntime_formatsHoursAndMinutes() {
        assertEquals("۲ ساعت و ۲۸ دقیقه", 148.faRuntime())
        assertEquals("۱ ساعت", 60.faRuntime())
        assertEquals("۴۵ دقیقه", 45.faRuntime())
    }

    @Test
    fun validation_acceptsValidAndRejectsInvalid() {
        assertTrue(Validation.isValidEmail("user@example.com"))
        assertFalse(Validation.isValidEmail("not-an-email"))
        assertFalse(Validation.isValidEmail("a@b"))
        assertTrue(Validation.isValidUsername("ali"))
        assertFalse(Validation.isValidUsername("ab"))
        assertTrue(Validation.isValidPassword("123456"))
        assertFalse(Validation.isValidPassword("12345"))
    }

    @Test
    fun passwordHasher_hashAndVerify() = runBlocking {
        val hash = PasswordHasher.hash("secret123".toCharArray())
        assertNotEquals("secret123", hash)
        assertTrue(PasswordHasher.verify("secret123".toCharArray(), hash))
        assertFalse(PasswordHasher.verify("wrong".toCharArray(), hash))
        // ساختار: iterations$salt$hash
        assertTrue(hash.split('$').size == 3)
    }

    @Test
    fun passwordHasher_differentSaltsForSamePassword() = runBlocking {
        val hashA = PasswordHasher.hash("same-password".toCharArray())
        val hashB = PasswordHasher.hash("same-password".toCharArray())
        assertNotEquals(hashA, hashB)
        assertTrue(PasswordHasher.verify("same-password".toCharArray(), hashA))
        assertTrue(PasswordHasher.verify("same-password".toCharArray(), hashB))
    }
}
