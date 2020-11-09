package com.aldidwikip.mygithubuser

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.edit
import com.aldidwikip.mygithubuser.util.AlarmReceiver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class MyGithubUser : Application() {
    private lateinit var alarmReceiver: AlarmReceiver
    @Inject lateinit var prefs: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        val x = prefs.getBoolean("key_x", true)
        if (x) {
            alarmReceiver = AlarmReceiver()
            alarmReceiver.setRepeatingAlarm(this, getString(R.string.reminder_time), getString(R.string.reminder_message))
            prefs.edit { putBoolean("key_x", false) }
        }
    }
}