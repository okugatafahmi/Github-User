package com.okugata.githubuser.detail_activity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.okugata.githubuser.model.User
import com.okugata.githubuser.util.getGithubAPI
import org.json.JSONArray
import java.lang.Exception

class UserDetailViewModel: ViewModel() {
    private val listUsers = MutableLiveData<ArrayList<User>>()

    fun getAPI(url: String){
        val users = ArrayList<User>()
        getGithubAPI(url){ error, response ->
            if (error != null) {
                error.printStackTrace()
                return@getGithubAPI
            }

            try {
                val items = JSONArray(response)

                for (i in 0 until items.length()) {
                    val item = items.getJSONObject(i)
                    val login = item.getString("login")
                    val avatarUrl = item.getString("avatar_url")
                    users.add(User(username = login, avatarUrl = avatarUrl))
                }
                listUsers.postValue(users)
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

}