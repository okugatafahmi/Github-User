package com.okugata.githubuser.activity.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.okugata.githubuser.R
import com.okugata.githubuser.activity.detail.UserDetailActivity
import com.okugata.githubuser.model.User
import com.okugata.githubuser.reminder.ReminderReceiver
import java.util.*

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                replace(R.id.settings, SettingsFragment())
            }
        }
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.settings)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
        private lateinit var reminderKey: String
        private lateinit var reminderPreference: SwitchPreference
        private lateinit var reminderReceiver: ReminderReceiver
        private lateinit var time: String

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            init()
            reminderReceiver = ReminderReceiver()
        }

        private fun init() {
            reminderKey = getString(R.string.key_reminder)
            time = getString(R.string.reminder_time)

            findPreference<Preference>(getString(R.string.key_developer))?.setOnPreferenceClickListener {
                val userDetailIntent = Intent(activity, UserDetailActivity::class.java)
                userDetailIntent.putExtra(
                    UserDetailActivity.EXTRA_USER, User(
                        username = getString(R.string.developer_username),
                        name = getString(R.string.developer_name)
                    )
                )
                startActivity(userDetailIntent)
                activity?.finish()
                true
            }

            findPreference<Preference>(getString(R.string.key_language))?.apply {
                summary = Locale.getDefault().displayName
                setOnPreferenceClickListener {
                    val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                    startActivity(mIntent)
                    true
                }
            }

            reminderPreference = findPreference<SwitchPreference>(reminderKey) as SwitchPreference
            reminderPreference.apply {
                title = getString(R.string.reminder_title, time)
                isChecked = preferenceManager.sharedPreferences.getBoolean(reminderKey, false)
            }
        }

        override fun onResume() {
            super.onResume()
            preferenceManager.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
        }

        override fun onPause() {
            super.onPause()
            preferenceManager.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        }

        override fun onSharedPreferenceChanged(
            sharedPreferences: SharedPreferences?,
            key: String?
        ) {
            if (key == reminderKey) {
                val isChecked = sharedPreferences?.getBoolean(key, false) ?: false
                reminderPreference.isChecked = isChecked
                if (isChecked) {
                    reminderReceiver.setRepeatingReminder(requireContext(), time, getString(R.string.find_user))
                }
                else {
                    reminderReceiver.cancelReminder(requireContext())
                }
            }
        }
    }
}