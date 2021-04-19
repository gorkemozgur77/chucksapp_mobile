package com.example.chuckapp.service

import android.content.Context
import android.content.SharedPreferences
import com.example.chuckapp.model.friend.Friend
import com.example.chuckapp.util.Constants

class SessionManager(val context: Context) {

    private var prefs: SharedPreferences =
        context.getSharedPreferences(Constants.TokenPreferenceFile, Context.MODE_PRIVATE)

    companion object {
        const val USER_TOKEN = "user_token"
        const val PERM_TOKEN = "perm_token"
    }


    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun savePermToken(token: String) {
        val editor = prefs.edit()
        editor.putString(PERM_TOKEN, token)
        editor.apply()
    }

    fun deleteTokens() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }


    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun fetchPermToken(): String? {
        return prefs.getString(PERM_TOKEN, null)
    }

}