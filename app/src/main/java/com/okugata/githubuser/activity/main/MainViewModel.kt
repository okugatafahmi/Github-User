package com.okugata.githubuser.activity.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.okugata.githubuser.model.User
import com.okugata.githubuser.util.getGithubAPI
import org.json.JSONObject
import java.lang.Exception

class MainViewModel: ViewModel() {
    private val _listUsers = MutableLiveData<ArrayList<User>>()
    val listUsers: LiveData<ArrayList<User>> = _listUsers

    fun searchUsername(username: String) {
        val users = ArrayList<User>()
        getGithubAPI("https://api.github.com/search/users?q=$username") { error, response ->
            if (error != null) {
                error.printStackTrace()
                return@getGithubAPI
            }

            try {
                val responseObject = JSONObject(response)
                val items = responseObject.getJSONArray("items")

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val login = item.getString("login")
                    val avatarUrl = item.getString("avatar_url")
                    users.add(User(username = login, avatarUrl = avatarUrl))
                }
                _listUsers.postValue(users)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun setUsers(users: ArrayList<User>) {
        _listUsers.postValue(users)
    }
}