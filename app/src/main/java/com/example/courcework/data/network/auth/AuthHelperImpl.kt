package com.example.courcework.data.network.auth

import android.annotation.SuppressLint
import android.content.SharedPreferences
import com.example.courcework.data.network.model.users.AuthCredentials
import javax.inject.Inject

class AuthHelperImpl @Inject constructor(
    private val sharedPreferences: SharedPreferences
) : AuthHelper {

    override val isAuthorized: Boolean get() = sharedPreferences.getBoolean(AUTH_STATUS_KEY, false)

    override val authHeader: String = "Authorization"
    override val credentials: String get() = okhttp3.Credentials.basic(
        sharedPreferences.getString(LOGIN_KEY, "")!!,
        sharedPreferences.getString(API_KEY, "")!!
    )
    override val userId get() = sharedPreferences.getInt(USER_ID_KEY, -1)

    @SuppressLint("ApplySharedPref")
    override fun setAuthData(authCredentials: AuthCredentials) {
        sharedPreferences.edit().apply {
            putString(LOGIN_KEY, authCredentials.login)
            putString(API_KEY, authCredentials.apiKey)
            commit()
        }
    }

    override fun setUserId(userId: Int) {
        sharedPreferences.edit().apply {
            putInt(USER_ID_KEY, userId)
            apply()
        }
    }

    override fun cleanAuthData() {
        sharedPreferences.edit().apply {
            remove(LOGIN_KEY)
            remove(API_KEY)
            remove(USER_ID_KEY)
            apply()
        }
    }

    override fun setAuthStatus(isAuthorized: Boolean) {
        sharedPreferences.edit().apply {
            putBoolean(AUTH_STATUS_KEY, isAuthorized)
            apply()
        }
    }

    companion object {
        const val SHARED_PREFERENCES_NAME = "SHARED_PREFERENCES_NAME"
        private const val LOGIN_KEY = "BASE_URL"
        private const val API_KEY = "API_KEY"
        private const val USER_ID_KEY = "USER_ID_KEY"
        private const val AUTH_STATUS_KEY = "AUTH_STATUS_KEY"
    }
}