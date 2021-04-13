package com.okugata.githubuser.activity.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.okugata.githubuser.GithubUserApplication
import com.okugata.githubuser.R
import com.okugata.githubuser.database.UserFavoriteViewModel
import com.okugata.githubuser.database.UserFavoriteViewModelFactory
import com.okugata.githubuser.databinding.ActivityUserDetailBinding
import com.okugata.githubuser.model.User


class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_followers,
            R.string.tab_following
        )
    }

    private lateinit var user: User
    private lateinit var binding: ActivityUserDetailBinding
    private var isFavorite = false
    private val userFavoriteViewModel: UserFavoriteViewModel by viewModels {
        UserFavoriteViewModelFactory((application as GithubUserApplication).userFavoriteRepository)
    }

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

        userFavoriteViewModel.allUser.observe(this) { users ->
            setFavorite(users.find { it.username == user.username } != null)
        }

        binding.fab.setOnClickListener {
            onClickFab()
        }
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
            userFavoriteViewModel.insert(User.toUserFavorite(user))
        }
        else {
            userFavoriteViewModel.delete(User.toUserFavorite(user))
        }
    }
}