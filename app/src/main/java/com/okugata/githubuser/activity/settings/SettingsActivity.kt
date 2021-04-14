package com.okugata.githubuser.activity.settings

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.okugata.githubuser.R
import com.okugata.githubuser.activity.detail.UserDetailActivity
import com.okugata.githubuser.model.User
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

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.preferences, rootKey)
            init()
        }

        private fun init() {
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
        }
    }
}