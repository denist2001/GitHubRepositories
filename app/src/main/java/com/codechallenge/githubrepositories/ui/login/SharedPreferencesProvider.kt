package com.codechallenge.githubrepositories.ui.login

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferencesProvider @Inject constructor(
    @ApplicationContext val context: Context,
    private val sharedPreferences: SharedPreferences
) {

    fun containsUser(user: String): Boolean {
        return sharedPreferences.contains(user)
    }

    fun getToken(user: String): String {
        return sharedPreferences.getString(user, "") ?: ""
    }

    fun addUser(user: String, accessToken: String) {
        if (containsUser(user)) {
            sharedPreferences.edit {
                remove(user)
            }
        }
        sharedPreferences.edit {
            putString(
                user,
                accessToken
            )
        }
    }
}