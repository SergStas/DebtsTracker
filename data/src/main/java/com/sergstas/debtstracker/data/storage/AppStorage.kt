package com.sergstas.debtstracker.data.storage

import android.content.SharedPreferences
import com.google.gson.Gson
import com.sergstas.debtstracker.domain.models.User
import javax.inject.Inject

class AppStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences,
) {
    companion object {
        private const val AUTHED_USER_KEY = "authedUser"
        private const val API_TOKEN_KEY = "token"
        private const val REFRESH_TOKEN_KEY = "refreshToken"
    }

    suspend fun getAuthedUser() =
        sharedPreferences.getString(AUTHED_USER_KEY, null)
            ?.let { Gson().fromJson(it, User::class.java) }

    suspend fun storeAuthedUser(user: User) =
        sharedPreferences.edit().putString(AUTHED_USER_KEY, Gson().toJson(user)).apply()

    suspend fun getApiToken() =
        sharedPreferences.getString(API_TOKEN_KEY, null)

    suspend fun storeApiToken(token: String) =
        sharedPreferences.edit().putString(API_TOKEN_KEY, token).apply()

    suspend fun getRefreshToken() =
        sharedPreferences.getString(REFRESH_TOKEN_KEY, null)

    suspend fun storeRefreshToken(refreshToken: String) =
        sharedPreferences.edit().putString(REFRESH_TOKEN_KEY, refreshToken).apply()
}