package com.example.umkmmart.data.pref

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("umkm_mart_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val IS_LOGGED_IN = "is_logged_in"
        private const val USER_EMAIL = "user_email"
    }

    fun saveLoginSession(email: String) {
        prefs.edit().apply {
            putBoolean(IS_LOGGED_IN, true)
            putString(USER_EMAIL, email)
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(IS_LOGGED_IN, false)
    }

    fun logout() {
        prefs.edit().clear().apply()
    }
}