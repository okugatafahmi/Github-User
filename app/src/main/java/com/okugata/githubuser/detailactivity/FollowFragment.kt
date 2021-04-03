package com.okugata.githubuser.detailactivity

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.okugata.githubuser.R
import com.okugata.githubuser.model.User
import com.okugata.githubuser.recyclerview.ListUserAdapter
import com.okugata.githubuser.util.getGithubAPI
import org.json.JSONArray
import java.lang.Exception

class FollowFragment : Fragment() {
    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"
        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private val apiList = arrayOf("followers", "following")
    private var users = arrayListOf<User>()
    private lateinit var rvUser: RecyclerView
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_follow, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0) ?: 0
        val username = arguments?.getString(ARG_USERNAME, "") ?: ""
        rvUser = view.findViewById(R.id.rv_user)
        progressBar = view.findViewById(R.id.progressBar)

        rvUser.setHasFixedSize(true)
        showRecyclerList()

        progressBar.visibility = View.VISIBLE
        getGithubAPI("https://api.github.com/users/$username/${apiList[index]}"){ error, response ->
            progressBar.visibility = View.INVISIBLE
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
                showRecyclerList()
            }catch (e: Exception){
                e.printStackTrace()
            }
        }
    }

    private fun showRecyclerList() {
        rvUser.layoutManager = LinearLayoutManager(context)
        val listUserAdapter = ListUserAdapter()
        listUserAdapter.setListUser(users)
        rvUser.adapter = listUserAdapter
    }
}