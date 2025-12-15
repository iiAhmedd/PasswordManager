package com.example.passwordmanager

object PasswordValidator {
    // Logic: Password is valid if it is not empty and at least 6 characters
    fun isStrong(password: String): Boolean {
        return password.isNotEmpty() && password.length >= 6
    }
}