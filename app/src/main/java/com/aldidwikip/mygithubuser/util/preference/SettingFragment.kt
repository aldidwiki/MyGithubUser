package com.aldidwikip.mygithubuser.util.preference

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.aldidwikip.mygithubuser.R
import com.aldidwikip.mygithubuser.util.AlarmReceiver
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject
import kotlin.properties.Delegates

@AndroidEntryPoint
class SettingFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener,
        Preference.OnPreferenceClickListener {
    private var reminder by Delegates.notNull<Boolean>()
    private var darkMode by Delegates.notNull<Boolean>()
    private lateinit var reminderPreference: SwitchPreference
    private lateinit var darkModePreference: SwitchPreference
    private lateinit var languagePreference: Preference
    private lateinit var keyReminder: String
    private lateinit var keyLanguage: String
    private lateinit var sysLanguage: String
    private lateinit var keyDarkMode: String
    @Inject lateinit var alarmReceiver: AlarmReceiver

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
        sysLanguage = Locale.getDefault().displayLanguage

        keyReminder = getString(R.string.key_reminder)
        keyLanguage = getString(R.string.key_language)
        keyDarkMode = getString(R.string.key_theme)

        init()
    }

    private fun init() {
        preferenceManager.sharedPreferences.apply {
            reminder = this.getBoolean(keyReminder, true)
            darkMode = this.getBoolean(keyDarkMode, false)
        }

        reminderPreference = findPreference<SwitchPreference>(keyReminder) as SwitchPreference
        darkModePreference = findPreference<SwitchPreference>(keyDarkMode) as SwitchPreference
        languagePreference = findPreference<Preference>(keyLanguage) as Preference

        languagePreference.onPreferenceClickListener = this

        reminderPreference.isChecked = reminder
        darkModePreference.isChecked = darkMode
        languagePreference.summary = sysLanguage
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

        if (key == keyDarkMode) {
            val isDarkModeOn = sharedPreferences?.getBoolean(keyDarkMode, false) as Boolean
            darkModePreference.isChecked = isDarkModeOn

            if (darkModePreference.isChecked)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
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

    override fun onPreferenceClick(preference: Preference?): Boolean {
        if (preference?.key == keyLanguage) {
            Intent(Settings.ACTION_LOCALE_SETTINGS).also {
                startActivity(it)
            }
        }

        return true
    }
}