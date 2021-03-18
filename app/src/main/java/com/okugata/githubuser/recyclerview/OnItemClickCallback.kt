package com.okugata.githubuser.recyclerview

import com.okugata.githubuser.model.User

interface OnItemClickCallback {
    fun onItemClicked(user: User)
}