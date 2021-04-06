package com.okugata.githubuser.detail_activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.annotation.StringRes
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayoutMediator
import com.okugata.githubuser.R
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (user.isGetAPI) {
            binding.progressBar.visibility = View.VISIBLE
            user.update {
                binding.progressBar.visibility = View.INVISIBLE
                doneLoading()
            }
        }
        else {
            doneLoading()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share_user ->shareUser()
            android.R.id.home->finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun doneLoading() {
        setInfo()
        setTabLayout()
    }

    private fun setInfo(){
        supportActionBar?.title = if (user.name.isNotEmpty())  user.name else user.username
        binding.tvItemUsername.text = user.username
        binding.tvItemRepository.text = resources.getQuantityString(R.plurals.numberOfRepository,
            user.repository, user.repository)

        val followers = resources.getQuantityString(R.plurals.numberOfFollower, user.followers,
            user.followers)
        val following = resources.getQuantityString(R.plurals.numberOfFollowing, user.following,
            user.following)
        binding.tvItemFollowersFollowing.text = resources.getString(R.string.followers_following,
            followers, "\u2022", following)

        binding.tvItemCompany.text = user.company
        binding.tvItemLocation.text = user.location

        if (user.avatarUrl.isNotEmpty()){
            Glide.with(applicationContext)
                .load(user.avatarUrl)
                .into(binding.imgItemPhoto)
        }
        else {
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
            putExtra(Intent.EXTRA_TEXT, resources.getQuantityString(R.plurals.numberOfRepositoryShareUser,
                user.repository, user.username, user.repository))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}