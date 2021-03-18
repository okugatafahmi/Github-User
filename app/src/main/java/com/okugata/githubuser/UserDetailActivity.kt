package com.okugata.githubuser

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import com.okugata.githubuser.databinding.ActivityUserDetailBinding
import com.okugata.githubuser.model.User

class UserDetailActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private lateinit var user: User
    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        user = intent.getParcelableExtra<User>(EXTRA_USER) as User
        supportActionBar?.title = user.name
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setInfo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home->finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setInfo(){
        binding.tvItemName.text = user.name
        var temp = "${user.username} \u2022 ${user.repository} Repositories"
        binding.tvItemUsernameRepository.text = temp

        temp = "${user.followers} Followers \u2022 ${user.following} Following"
        binding.tvItemFollowersFollowing.text = temp

        binding.tvItemCompany.text = user.company
        binding.tvItemLocation.text = user.location

        binding.imgItemPhoto.setImageResource(user.avatar!!)
    }
}