package com.joostleo.linguadailyapp.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import com.joostleo.linguadailyapp.datamodels.Language

class LanguagePreferencesManager(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("selected_languages", Context.MODE_PRIVATE)

    fun getLanguages(): Set<Language> {
        val storedCodes = sharedPreferences.getStringSet("SELECTED_LANGUAGES", setOf("en")) ?: emptySet()
        return storedCodes.mapNotNull { Language.fromCountryCode(it) }.toSet()
    }

    fun setLanguages(languages: Set<Language>) {
        val codesToSave = languages.map { it.code }.toSet()
        sharedPreferences.edit().apply {
            putStringSet("SELECTED_LANGUAGES", codesToSave)
            apply()
        }
    }
}