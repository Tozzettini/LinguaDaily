package com.example.linguadailyapp.utils.preferences

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.linguadailyapp.datamodels.Language
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class PreferencesManager(context: Context) {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("preferences", Context.MODE_PRIVATE)

    // ---- Default values ----
    private val DEFAULT_NOTIFICATIONS_ENABLED = false
    private val DEFAULT_SYNC_ON_DATA = true
    private val DEFAULT_FIRST_LAUNCH = true
    private val DEFAULT_OUT_OF_WORDS = true
    private val DEFAULT_SKIP = 0

    // New keys for vocabulary statistics
    private  val KEY_WORDS_LEARNED = "words_learned"
    private  val KEY_TOTAL_GOAL = "total_goal"
    private  val KEY_STREAK_DAYS = "streak_days"
    private  val KEY_LAST_OPENED_DATE = "last_opened_date"

    // New methods for vocabulary statistics

    fun getWordsLearned(): Int {
        return sharedPreferences.getInt(KEY_WORDS_LEARNED, 0)
    }

    fun setWordsLearned(count: Int) {
        sharedPreferences.edit() { putInt(KEY_WORDS_LEARNED, count) }
    }

    fun getTotalGoal(): Int {
        return sharedPreferences.getInt(KEY_TOTAL_GOAL, 365)
    }

    fun setTotalGoal(goal: Int) {
        sharedPreferences.edit() { putInt(KEY_TOTAL_GOAL, goal) }
    }

    fun getStreakDays(): Int {
        return sharedPreferences.getInt(KEY_STREAK_DAYS, 0)
    }

    fun setStreakDays(days: Int) {
        sharedPreferences.edit() { putInt(KEY_STREAK_DAYS, days) }
    }

    fun getLastOpenedDate(): LocalDate? {
        val dateString = sharedPreferences.getString(KEY_LAST_OPENED_DATE, null)
        return if (dateString != null) {
            LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE)
        } else {
            null
        }
    }

    fun setLastOpenedDate(date: LocalDate?) {
        sharedPreferences.edit() {
            if (date != null) {
                putString(KEY_LAST_OPENED_DATE, date.format(DateTimeFormatter.ISO_LOCAL_DATE))
            } else {
                remove(KEY_LAST_OPENED_DATE)
            }
        }
    }

    // ---- Methods to access settings ----

    fun setNotificationsEnabled(enabled: Boolean) {
        sharedPreferences.edit()
            .putBoolean("notifications_enabled", enabled)
            .apply()
    }

    fun isNotificationsEnabled(): Boolean {
        return sharedPreferences.getBoolean("notifications_enabled", DEFAULT_NOTIFICATIONS_ENABLED)
    }

    fun setAllowSyncOnData(value: Boolean) {
        sharedPreferences.edit()
            .putBoolean("sync_on_data", value)
            .apply()
    }

    fun getSyncAllowedOnData(): Boolean {
        return sharedPreferences.getBoolean("sync_on_data", DEFAULT_SYNC_ON_DATA)
    }

    fun isFirstLaunch(): Boolean {
        val isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", DEFAULT_FIRST_LAUNCH)

        if (isFirstLaunch) {
            sharedPreferences.edit().putBoolean("is_first_launch", false).apply()
        }

        return isFirstLaunch
    }

    fun setOutOfWordsMode(value: Boolean) {
        sharedPreferences.edit()
            .putBoolean("is_out_of_words", value)
            .apply()
    }

    fun getOutOfWordsMode() : Boolean {
        return sharedPreferences.getBoolean("is_out_of_words", DEFAULT_OUT_OF_WORDS)
    }

    fun setSkipForLanguage(skip: Int, language: Language) {
        sharedPreferences.edit()
            .putInt("skip_${language.ordinal}", skip)
            .apply()
    }

    fun getSkipForLanguage(language: Language): Int {
        return sharedPreferences.getInt("skip_${language.ordinal}", DEFAULT_SKIP)
    }


}