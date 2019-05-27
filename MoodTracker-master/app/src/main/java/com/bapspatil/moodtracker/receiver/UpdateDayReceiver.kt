package com.bapspatil.moodtracker.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager

import com.bapspatil.moodtracker.data.SharedPreferencesHelper

class UpdateDayReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val mPreferences = PreferenceManager.getDefaultSharedPreferences(context)

        var currentDay = mPreferences.getInt(SharedPreferencesHelper.KEY_CURRENT_DAY, 1)
        currentDay++
        mPreferences.edit().putInt(SharedPreferencesHelper.KEY_CURRENT_DAY, currentDay).apply()
    }
}
