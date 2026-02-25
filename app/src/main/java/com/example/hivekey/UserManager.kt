package com.example.hivekey

import android.content.Context
import android.content.SharedPreferences
import java.security.MessageDigest

/**
 * Local user manager using SharedPreferences.
 * Handles registration, login, and profile storage entirely on-device.
 */
object UserManager {

    private const val PREFS_NAME = "hivekey_users"
    private const val CURRENT_USER_KEY = "current_user_email"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /** Hash password using SHA-256 */
    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    /**
     * Register a new user. Returns true if successful, false if email already exists.
     */
    fun register(
        context: Context,
        username: String,
        email: String,
        password: String,
        role: String = "user"
    ): Boolean {
        val prefs = getPrefs(context)
        val key = "user_${email.lowercase()}"

        // Check if user already exists
        if (prefs.contains("${key}_email")) {
            return false
        }

        prefs.edit().apply {
            putString("${key}_username", username)
            putString("${key}_email", email.lowercase())
            putString("${key}_password", hashPassword(password))
            putString("${key}_role", role)
            putString("${key}_photoUrl", "")
            putLong("${key}_createdAt", System.currentTimeMillis())
            apply()
        }
        return true
    }

    /**
     * Login with email and password. Returns true if credentials match.
     */
    fun login(context: Context, email: String, password: String): Boolean {
        val prefs = getPrefs(context)
        val key = "user_${email.lowercase()}"

        val storedPassword = prefs.getString("${key}_password", null) ?: return false
        return if (storedPassword == hashPassword(password)) {
            // Save current logged-in user
            prefs.edit().putString(CURRENT_USER_KEY, email.lowercase()).apply()
            true
        } else {
            false
        }
    }

    /** Get the currently logged-in user's email, or null if not logged in */
    fun getCurrentUserEmail(context: Context): String? {
        return getPrefs(context).getString(CURRENT_USER_KEY, null)
    }

    /** Check if a user is currently logged in */
    fun isLoggedIn(context: Context): Boolean {
        return getCurrentUserEmail(context) != null
    }

    /** Logout the current user */
    fun logout(context: Context) {
        getPrefs(context).edit().remove(CURRENT_USER_KEY).apply()
    }

    /** Get a user profile field */
    fun getUserField(context: Context, field: String): String {
        val email = getCurrentUserEmail(context) ?: return ""
        val key = "user_${email}"
        return getPrefs(context).getString("${key}_$field", "") ?: ""
    }

    /** Get user creation timestamp */
    fun getUserCreatedAt(context: Context): Long {
        val email = getCurrentUserEmail(context) ?: return 0L
        val key = "user_${email}"
        return getPrefs(context).getLong("${key}_createdAt", 0L)
    }

    /** Update a user profile field (e.g., photoUrl) */
    fun updateUserField(context: Context, field: String, value: String) {
        val email = getCurrentUserEmail(context) ?: return
        val key = "user_${email}"
        getPrefs(context).edit().putString("${key}_$field", value).apply()
    }
}
