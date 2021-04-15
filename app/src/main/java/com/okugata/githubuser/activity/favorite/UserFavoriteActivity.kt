package com.okugata.githubuser.activity.favorite

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.okugata.githubuser.GithubUserApplication
import com.okugata.githubuser.R
import com.okugata.githubuser.activity.detail.UserDetailActivity
import com.okugata.githubuser.database.UserFavoriteViewModel
import com.okugata.githubuser.database.UserFavoriteViewModelFactory
import com.okugata.githubuser.databinding.ActivityUserFavoriteBinding
import com.okugata.githubuser.model.User
import com.okugata.githubuser.recyclerview.ListUserAdapter
import com.okugata.githubuser.recyclerview.OnItemClickCallback

class UserFavoriteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUserFavoriteBinding
    private val userFavoriteViewModel: UserFavoriteViewModel by viewModels {
        UserFavoriteViewModelFactory((application as GithubUserApplication).userFavoriteDao)
    }

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

        userFavoriteViewModel.allUser.observe(this) { users ->
            users?.let { adapter.setListUser(User.fromUserFavoriteList(it)) }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return true
    }
}