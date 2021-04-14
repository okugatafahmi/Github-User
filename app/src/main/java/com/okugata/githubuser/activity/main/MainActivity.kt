package com.okugata.githubuser.activity.main

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.content.res.TypedArray
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.okugata.githubuser.R
import com.okugata.githubuser.activity.detail.UserDetailActivity
import com.okugata.githubuser.activity.favorite.UserFavoriteActivity
import com.okugata.githubuser.activity.settings.SettingsActivity
import com.okugata.githubuser.databinding.ActivityMainBinding
import com.okugata.githubuser.model.User
import com.okugata.githubuser.recyclerview.ListUserAdapter
import com.okugata.githubuser.recyclerview.OnItemClickCallback


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ListUserAdapter

    private lateinit var dataUsername: Array<String>
    private lateinit var dataName: Array<String>
    private lateinit var dataLocation: Array<String>
    private lateinit var dataRepository: Array<String>
    private lateinit var dataCompany: Array<String>
    private lateinit var dataFollowers: Array<String>
    private lateinit var dataFollowing: Array<String>
    private lateinit var dataAvatar: TypedArray

    private lateinit var mainViewModel: MainViewModel
    private var users = arrayListOf<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = ListUserAdapter().apply {
            setOnItemClickCallback(object : OnItemClickCallback {
                override fun onItemClicked(user: User) {
                    val userDetailIntent = Intent(this@MainActivity, UserDetailActivity::class.java)
                    userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
                    startActivity(userDetailIntent)
                }
            })
        }

        binding.rvUser.setHasFixedSize(true)
        binding.rvUser.layoutManager = LinearLayoutManager(this)
        binding.rvUser.adapter = adapter

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(MainViewModel::class.java)

        prepare()
        addItem()
        adapter.setListUser(users)

        mainViewModel.listUsers.observe(this) { userItems ->
            adapter.setListUser(userItems)
            showLoading(false)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        setSearchView(menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val userFavoriteIntent = Intent(this@MainActivity, UserFavoriteActivity::class.java)
                startActivity(userFavoriteIntent)
            }
            R.id.settings -> {
                val mIntent = Intent(this@MainActivity, SettingsActivity::class.java)
                startActivity(mIntent)
            }
            R.id.action_reset_list -> {
                mainViewModel.setUsers(users)
            }
        }
        return true
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            adapter.setListUser(ArrayList())
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setSearchView(menu: Menu) {
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchItem = menu.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                setItemsVisibility(menu, searchItem, false)
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                setItemsVisibility(menu, searchItem, true)
                invalidateOptionsMenu()
                return true
            }
        })

        searchView.apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            queryHint = resources.getString(R.string.search_hint)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    showLoading(true)
                    searchView.clearFocus()
                    mainViewModel.searchUsername(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
    }

    private fun setItemsVisibility(menu: Menu, exception: MenuItem, isVisible: Boolean) {
        for (i in 0 until menu.size()) {
            val item = menu.getItem(i)
            if (item !== exception) item.isVisible = isVisible
        }
    }

    private fun prepare() {
        dataUsername = resources.getStringArray(R.array.username)
        dataName = resources.getStringArray(R.array.name)
        dataLocation = resources.getStringArray(R.array.location)
        dataRepository = resources.getStringArray(R.array.repository)
        dataCompany = resources.getStringArray(R.array.company)
        dataFollowers = resources.getStringArray(R.array.followers)
        dataFollowing = resources.getStringArray(R.array.following)
        dataAvatar = resources.obtainTypedArray(R.array.avatar)
    }

    private fun addItem() {
        for (position in dataUsername.indices) {
            val user = User(
                dataUsername[position],
                dataName[position],
                dataLocation[position],
                dataRepository[position].toInt(),
                dataCompany[position],
                dataFollowers[position].toInt(),
                dataFollowing[position].toInt(),
                dataAvatar.getResourceId(position, -1),
                isGetAPI = false
            )
            users.add(user)
        }
    }
}