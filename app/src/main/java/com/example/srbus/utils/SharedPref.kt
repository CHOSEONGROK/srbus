package com.example.srbus.utils

import android.content.Context
import android.content.Context.MODE_PRIVATE

class SharedPref {

    enum class Key {RECENT_SEARCH_MODE}

    /**
    * [Key.RECENT_SEARCH_MODE]
    */
    enum class RecentSearchMode {BUS, STATION}

    companion object {
        private const val FILE_NAME = "pref"

        fun getStringPreferences(context: Context, key: Key, defValue: String = ""): String? = context
            .getSharedPreferences(FILE_NAME, MODE_PRIVATE)
            .getString(key.name, defValue)

        fun saveStringPreferences(context: Context, key: Key, value: String) {
            context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                .edit()
                .putString(key.name, value)
                .apply()
        }

        fun removePreferences(context: Context, key: Key) {
            context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                .edit()
                .remove(key.name)
                .apply()
        }

        fun removeAllPreferences(context: Context) {
            context.getSharedPreferences(FILE_NAME, MODE_PRIVATE)
                .edit()
                .clear()
                .apply()
        }


    }
}