package com.okugata.githubuserfavorite.recyclerview

import com.okugata.githubuserfavorite.model.User

interface OnItemClickCallback {
    fun onItemClicked(user: User)
}