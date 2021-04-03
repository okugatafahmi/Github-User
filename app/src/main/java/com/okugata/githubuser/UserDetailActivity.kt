package com.okugata.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.bumptech.glide.Glide
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
        supportActionBar?.title = user.username
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (user.isGetAPI) {
            binding.progressBar.visibility = View.VISIBLE
            user.update {
                binding.progressBar.visibility = View.INVISIBLE
                setInfo()
            }
        }
        else {
            setInfo()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_user_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.share_user->shareUser()
            android.R.id.home->finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setInfo(){
        binding.tvItemUsername.text = user.username
        var temp = "${user.name} \u2022 ${user.repository} repositories"
        binding.tvItemNameRepository.text = temp

        temp = "${user.followers} followers \u2022 ${user.following} following"
        binding.tvItemFollowersFollowing.text = temp

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

    private fun shareUser() {
        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, resources.getString(R.string.share_user, user.username,
                user.repository))
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(sendIntent, null)
        startActivity(shareIntent)
    }
}