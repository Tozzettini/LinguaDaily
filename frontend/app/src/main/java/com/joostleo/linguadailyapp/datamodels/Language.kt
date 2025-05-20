package com.joostleo.linguadailyapp.datamodels

enum class Language(val displayName: String, val code: String, val flag: String) {
    SPANISH("Español", "es", "🇪🇸"),
    ENGLISH("English", "en", "🇺🇸"),
    ENGLISH_PLUS("English+", "en+", "🇺🇸"),
    GERMAN("Deutsch", "de", "🇩🇪"),
    ITALIAN("Italiano", "it", "🇮🇹"),
    DUTCH("Nederlands", "nl", "🇳🇱");

    override fun toString(): String = displayName

    companion object {
        fun fromDisplayName(name: String): Language? =
            Language.entries.firstOrNull { it.displayName.equals(name, ignoreCase = true) }

        fun fromCountryCode(code: String): Language? =
            Language.entries.firstOrNull { it.code.equals(code, ignoreCase = true) }
    }
}