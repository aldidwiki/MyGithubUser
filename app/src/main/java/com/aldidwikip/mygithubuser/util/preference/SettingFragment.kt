package com.aldidwikip.mygithubuser.util.preference

import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.util.AlarmReceiver
import kotlin.properties.Delegates

class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    private var reminder by Delegates.notNull<Boolean>()
    private lateinit var language: String
    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: ListPreference
    private lateinit var keyReminder: String
    private lateinit var keyLanguage: String
    private lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)

        keyReminder = getString(R.string.key_reminder)
        keyLanguage = getString(R.string.key_language)
        alarmReceiver = AlarmReceiver()

        init()
    }

    private fun init() {
        preferenceManager.sharedPreferences.apply {
            reminder = this.getBoolean(keyReminder, true)
            language = this.getString(keyLanguage, "English").toString()
        }

        reminderPreference = findPreference<SwitchPreference>(keyReminder) as SwitchPreference
        languagePreference = findPreference<ListPreference>(keyLanguage) as ListPreference

        reminderPreference.isChecked = reminder
        languagePreference.summary = language
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == keyReminder) {
            val isReminderOn = sharedPreferences?.getBoolean(keyReminder, true) as Boolean
            reminderPreference.isChecked = isReminderOn

            if (reminderPreference.isChecked) {
                alarmReceiver.setRepeatingAlarm(requireActivity(), getString(R.string.reminder_time), getString(R.string.reminder_message))
            } else {
                alarmReceiver.cancelAlarm(requireActivity())
            }
        }

        if (key == keyLanguage) {
            Toast.makeText(activity, sharedPreferences?.getString(keyLanguage, "English"), Toast.LENGTH_SHORT).show()
            languagePreference.summary = sharedPreferences?.getString(keyLanguage, "English")
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }
}