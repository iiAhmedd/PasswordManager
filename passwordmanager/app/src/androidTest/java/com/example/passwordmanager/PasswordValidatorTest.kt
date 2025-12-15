package com.example.passwordmanager

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class PasswordValidatorTest {

    @Test
    fun password_tooShort_returnsFalse() {

        val result = PasswordValidator.isStrong("12345")
        assertFalse(result)
    }

    @Test
    fun password_valid_returnsTrue() {
        
        val result = PasswordValidator.isStrong("SecurePass123")
        assertTrue(result)
    }
}