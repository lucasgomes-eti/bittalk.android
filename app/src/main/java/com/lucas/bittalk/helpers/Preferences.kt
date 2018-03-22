package com.lucas.bittalk.helpers

import android.content.Context

/**
 * Created by lucas on 19/11/2017.
 */
class Preferences(context: Context){

    companion object {
        val KEY_USER_ID = "token"
        val KEY_ONE_SIGNAL_USER_ID = "oneSignalUserId"
    }

    private val preferences = context.getSharedPreferences("bittalk.preferences", Context.MODE_PRIVATE)

    fun saveUserPreferences(userId: String) {
        preferences.edit().clear().apply()
        preferences.edit().putString(KEY_USER_ID, userId).apply()
//        preferences.edit().putString(KEY_NAME, name).apply()
    }

    fun getUserPreferences() : HashMap<String, String> {
        val userPreferences = hashMapOf<String, String>()
        userPreferences.put(KEY_USER_ID, preferences.getString(KEY_USER_ID, ""))
//        userPreferences.put(KEY_NAME, preferences.getString(KEY_NAME, ""))
        return userPreferences
    }

    fun getCurrentUserId() = preferences.getString(KEY_USER_ID, "")

    fun saveOneSignalUserId(oneSignalUserId: String) {
        preferences.edit().putString(KEY_ONE_SIGNAL_USER_ID, oneSignalUserId).apply()
    }

    fun getOneSignalUserId() = preferences.getString(KEY_ONE_SIGNAL_USER_ID, "")
}