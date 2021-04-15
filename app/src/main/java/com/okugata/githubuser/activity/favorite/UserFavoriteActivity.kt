package com.okugata.githubuser.activity.favorite

import android.content.Intent
import android.database.ContentObserver
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.okugata.githubuser.R
import com.okugata.githubuser.activity.detail.UserDetailActivity
import com.okugata.githubuser.provider.UserFavoriteProvider
import com.okugata.githubuser.database.UserFavorite
import com.okugata.githubuser.databinding.ActivityUserFavoriteBinding
import com.okugata.githubuser.model.User
import com.okugata.githubuser.recyclerview.ListUserAdapter
import com.okugata.githubuser.recyclerview.OnItemClickCallback
import com.okugata.githubuser.util.MappingHelper
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
            setDisplayHomeAsUpEnabled(true)
            title = getString(R.string.favorite)
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
            UserFavoriteProvider.USER_FAVORITE_URI,
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }

    private fun loadUserFavoritesAsync(callback: (ArrayList<UserFavorite>) -> Unit) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUserFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(
                    UserFavoriteProvider.USER_FAVORITE_URI,
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