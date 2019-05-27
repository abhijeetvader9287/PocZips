package com.bapspatil.moodtracker.data

/*
 ** Created by Bapusaheb Patil {@link https://bapspatil.com}
 */

import android.content.SharedPreferences

object SharedPreferencesHelper {

    val KEY_CURRENT_DAY = "KEY_CURRENT_DAY"

    val KEY_CURRENT_MOOD = "KEY_CURRENT_MOOD"
    val KEY_CURRENT_COMMENT = "KEY_CURRENT_COMMENT"

    val KEY_MOOD0 = "KEY_MOOD0"
    val KEY_MOOD1 = "KEY_MOOD1"
    val KEY_MOOD2 = "KEY_MOOD2"
    val KEY_MOOD3 = "KEY_MOOD3"
    val KEY_MOOD4 = "KEY_MOOD4"
    val KEY_MOOD5 = "KEY_MOOD5"
    val KEY_MOOD6 = "KEY_MOOD6"

    val KEY_COMMENT0 = "KEY_COMMENT0"
    val KEY_COMMENT1 = "KEY_COMMENT1"
    val KEY_COMMENT2 = "KEY_COMMENT2"
    val KEY_COMMENT3 = "KEY_COMMENT3"
    val KEY_COMMENT4 = "KEY_COMMENT4"
    val KEY_COMMENT5 = "KEY_COMMENT5"
    val KEY_COMMENT6 = "KEY_COMMENT6"

    fun saveMood(moodIndex: Int, currentDay: Int, sp: SharedPreferences) {
        sp.edit().putInt(KEY_CURRENT_MOOD, moodIndex).apply()
        when (currentDay) {
            1 -> sp.edit().putInt(KEY_MOOD0, moodIndex).apply()
            2 -> sp.edit().putInt(KEY_MOOD1, moodIndex).apply()
            3 -> sp.edit().putInt(KEY_MOOD2, moodIndex).apply()
            4 -> sp.edit().putInt(KEY_MOOD3, moodIndex).apply()
            5 -> sp.edit().putInt(KEY_MOOD4, moodIndex).apply()
            6 -> sp.edit().putInt(KEY_MOOD5, moodIndex).apply()
            7 -> sp.edit().putInt(KEY_MOOD6, moodIndex).apply()
            else -> {
                sp.edit().putInt(KEY_MOOD0, sp.getInt(KEY_MOOD1, 3)).apply()
                sp.edit().putInt(KEY_MOOD1, sp.getInt(KEY_MOOD2, 3)).apply()
                sp.edit().putInt(KEY_MOOD2, sp.getInt(KEY_MOOD3, 3)).apply()
                sp.edit().putInt(KEY_MOOD3, sp.getInt(KEY_MOOD4, 3)).apply()
                sp.edit().putInt(KEY_MOOD4, sp.getInt(KEY_MOOD5, 3)).apply()
                sp.edit().putInt(KEY_MOOD5, sp.getInt(KEY_MOOD6, 3)).apply()
                sp.edit().putInt(KEY_MOOD6, moodIndex).apply()
            }
        }
    }

    fun saveComment(comment: String, currentDay: Int, sp: SharedPreferences) {
        sp.edit().putString(KEY_CURRENT_COMMENT, comment).apply()
        when (currentDay) {
            1 -> sp.edit().putString(KEY_COMMENT0, comment).apply()
            2 -> sp.edit().putString(KEY_COMMENT1, comment).apply()
            3 -> sp.edit().putString(KEY_COMMENT2, comment).apply()
            4 -> sp.edit().putString(KEY_COMMENT3, comment).apply()
            5 -> sp.edit().putString(KEY_COMMENT4, comment).apply()
            6 -> sp.edit().putString(KEY_COMMENT5, comment).apply()
            7 -> sp.edit().putString(KEY_COMMENT6, comment).apply()
            else -> {
                sp.edit().putString(KEY_COMMENT0, sp.getString(KEY_COMMENT1, "")).apply()
                sp.edit().putString(KEY_COMMENT1, sp.getString(KEY_COMMENT2, "")).apply()
                sp.edit().putString(KEY_COMMENT2, sp.getString(KEY_COMMENT3, "")).apply()
                sp.edit().putString(KEY_COMMENT3, sp.getString(KEY_COMMENT4, "")).apply()
                sp.edit().putString(KEY_COMMENT4, sp.getString(KEY_COMMENT5, "")).apply()
                sp.edit().putString(KEY_COMMENT5, sp.getString(KEY_COMMENT6, "")).apply()
                sp.edit().putString(KEY_COMMENT6, comment).apply()
            }
        }
    }
}
