package com.aldidwikip.mygithubuser

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.aldidwikip.mygithubuser.util.AlarmReceiver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyGithubUser : Application() {
    @Inject lateinit var alarmReceiver: AlarmReceiver
    @Inject lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        val x = prefs.getBoolean("key_x", true)
        if (x) {
            alarmReceiver.setRepeatingAlarm(this, getString(R.string.reminder_time), getString(R.string.reminder_message))
            prefs.edit { putBoolean("key_x", false) }
        }

        val isDarkModeOn = PreferenceManager.getDefaultSharedPreferences(this)
                .getBoolean(getString(R.string.key_theme), false)

        if (isDarkModeOn)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
    }
}