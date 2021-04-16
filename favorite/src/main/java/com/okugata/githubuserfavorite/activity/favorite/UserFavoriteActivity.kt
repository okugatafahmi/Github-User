package com.okugata.githubuserfavorite.activity.favorite

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.okugata.githubuserfavorite.R
import com.okugata.githubuserfavorite.activity.detail.UserDetailActivity
import com.okugata.githubuserfavorite.activity.settings.SettingsActivity
import com.okugata.githubuserfavorite.database.UserFavorite
import com.okugata.githubuserfavorite.databinding.ActivityUserFavoriteBinding
import com.okugata.githubuserfavorite.model.User
import com.okugata.githubuserfavorite.recyclerview.ListUserAdapter
import com.okugata.githubuserfavorite.recyclerview.OnItemClickCallback
import com.okugata.githubuserfavorite.util.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFavoriteBinding
    private lateinit var observer: ContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            title = getString(R.string.app_name)
        }

        val adapter = ListUserAdapter().apply {
            setOnItemClickCallback(object : OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    val userDetailIntent =
                        Intent(this@UserFavoriteActivity, UserDetailActivity::class.java)
                    userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
                    startActivity(userDetailIntent)
                }
            })
        }

        binding.rvUser.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@UserFavoriteActivity)
        }
        binding.rvUser.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        observer = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserFavoritesAsync {
                    adapter.setListUser(User.fromUserFavoriteList(it))
                }
            }
        }

        contentResolver.registerContentObserver(
            Uri.parse("content://com.okugata.githubuser/user_favorite"),
            true,
            observer
        )

        loadUserFavoritesAsync {
            adapter.setListUser(User.fromUserFavoriteList(it))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(observer)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_user_favorite, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.settings -> {
                val mIntent = Intent(this@UserFavoriteActivity, SettingsActivity::class.java)
                startActivity(mIntent)
            }
        }
        return true
    }

    private fun loadUserFavoritesAsync(callback: (ArrayList<UserFavorite>) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUserFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(
                    Uri.parse("content://com.okugata.githubuser/user_favorite"),
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val users = deferredUserFavorites.await()
            callback(users)
        }
    }
}