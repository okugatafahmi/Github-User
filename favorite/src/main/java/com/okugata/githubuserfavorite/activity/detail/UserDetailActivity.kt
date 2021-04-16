package com.okugata.githubuserfavorite.activity.detail

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.okugata.githubuserfavorite.R
import com.okugata.githubuserfavorite.databinding.ActivityUserDetailBinding
import com.okugata.githubuserfavorite.model.User
import com.okugata.githubuserfavorite.util.MappingHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
        val USER_FAVORITE_URI: Uri = Uri.parse("content://com.okugata.githubuser/user_favorite")

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private lateinit var user: User
    private lateinit var binding: ActivityUserDetailBinding
    private var isFavorite = false
    private lateinit var observer: ContentObserver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.collapsingToolbar.setCollapsedTitleTextColor(
            ContextCompat.getColor(this, R.color.white)
        )
        binding.collapsingToolbar.setExpandedTitleColor(
            ContextCompat.getColor(this, R.color.grey)
        )

        binding.scrollView.isFillViewport = true

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User

        if (user.isGetAPI) {
            binding.progressBar.visibility = View.VISIBLE
            user.update {
                binding.progressBar.visibility = View.GONE
                setInfo()
            }
        } else {
            setInfo()
        }
        setTabLayout()

        binding.fab.setOnClickListener {
            onClickFab()
        }

        setObserverUserFavorites()
        checkUserFavorites()
    }

    override fun onDestroy() {
        super.onDestroy()
        contentResolver.unregisterContentObserver(observer)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.share_user -> shareUser()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setInfo() {
        binding.collapsingToolbar.title = if (user.name.isNotEmpty()) user.name else user.username
        binding.tvItemUsername.text = user.username
        binding.tvItemRepository.text = resources.getQuantityString(
            R.plurals.numberOfRepository,
            user.repository, user.repository
        )

        val followers = resources.getQuantityString(
            R.plurals.numberOfFollower, user.followers,
            user.followers
        )
        val following = resources.getQuantityString(
            R.plurals.numberOfFollowing, user.following,
            user.following
        )
        binding.tvItemFollowersFollowing.text = resources.getString(
            R.string.followers_following,
            followers, "\u2022", following
        )

        binding.tvItemCompany.text = user.company
        binding.tvItemLocation.text = user.location

        if (user.avatarUrl.isNotEmpty()) {
            Glide.with(applicationContext)
                .load(user.avatarUrl)
                .into(binding.imgItemPhoto)
        } else {
            binding.imgItemPhoto.setImageResource(user.avatar)
        }
    }

    private fun setTabLayout() {
        val sectionsPagerAdapter = SectionsPagerAdapter(this, user.username)
        val viewPager = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs = binding.tabs
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun shareUser() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT, resources.getQuantityString(
                    R.plurals.numberOfRepositoryShareUser,
                    user.repository, user.username, user.repository
                )
            )
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }

    private fun setFavorite(state: Boolean) {
        isFavorite = state
        if (state) {
            binding.fab.setImageResource(R.drawable.ic_favorite_white_24dp)
        } else {
            binding.fab.setImageResource(R.drawable.ic_favorite_border_white_24dp)
        }
    }

    private fun onClickFab() {
        if (!isFavorite) {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredUri = async(Dispatchers.IO) {
                    contentResolver.insert(
                        Uri.parse(USER_FAVORITE_URI.toString()),
                        user.toUserFavorite().toContentValues()
                    )
                }
                deferredUri.await()?.also {
                    val id = it.lastPathSegment.toString().toLong()
                    if (id < 1L) return@also
                    user.id = id
                    setFavorite(true)
                }
            }
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                val deferredDeleted = async(Dispatchers.IO) {
                    contentResolver.delete(
                        Uri.parse("$USER_FAVORITE_URI/${user.id ?: 0}"),
                        null,
                        null
                    )
                }
                deferredDeleted.await().also {
                    setFavorite(it != 1)
                }
            }
        }
    }

    private fun setObserverUserFavorites() {
        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        observer = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                checkUserFavorites()
            }
        }

        // register URI of this user if there is update
        contentResolver.registerContentObserver(
            Uri.parse(USER_FAVORITE_URI.toString()),
            true,
            observer
        )
    }

    private fun checkUserFavorites() {
        // Get all favorite & check if current user in favorite
        GlobalScope.launch(Dispatchers.Main) {
            val deferredUserFavorites = async(Dispatchers.IO) {
                val cursor = contentResolver.query(
                    USER_FAVORITE_URI,
                    null,
                    null,
                    null,
                    null
                )
                MappingHelper.mapCursorToArrayList(cursor)
            }
            deferredUserFavorites.await().also { users ->
                val item = users.find { it.username == user.username }
                setFavorite(item != null)
                user.id = item?.id
            }
        }
    }
}