package com.okugata.githubuser.activity.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.okugata.githubuser.R
import com.okugata.githubuser.model.User
import com.okugata.githubuser.recyclerview.ListUserAdapter
import com.okugata.githubuser.recyclerview.OnItemClickCallback

class FollowFragment : Fragment() {
    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"
        private const val ARG_USERNAME = "username"
        private val API_LIST = arrayOf("followers", "following")

        @JvmStatic
        fun newInstance(index: Int, username: String) =
            FollowFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, index)
                    putString(ARG_USERNAME, username)
                }
            }
    }

    private lateinit var rvUser: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var followViewModel: FollowViewModel


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

        val adapter = ListUserAdapter().apply {
            setOnItemClickCallback(object : OnItemClickCallback{
                override fun onItemClicked(user: User) {
                    val userDetailIntent = Intent(requireContext(), UserDetailActivity::class.java)
                    userDetailIntent.putExtra(UserDetailActivity.EXTRA_USER, user)
                    startActivity(userDetailIntent)
                }
            })
        }

        rvUser.setHasFixedSize(true)
        rvUser.layoutManager = LinearLayoutManager(activity)
        rvUser.adapter = adapter

        progressBar.visibility = View.VISIBLE

        followViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())
            .get(FollowViewModel::class.java)
        followViewModel.listUsers.observe(viewLifecycleOwner){ userItems ->
            if (userItems != null) {
                adapter.setListUser(userItems)
                progressBar.visibility = View.GONE
            }
        }

        followViewModel.getAPI("https://api.github.com/users/$username/${API_LIST[index]}")
    }
}