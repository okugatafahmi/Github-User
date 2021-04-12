package com.okugata.githubuser.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_favorite")
data class UserFavorite(
    @PrimaryKey val username: String,
    val name: String = "",
    val location: String = "No Location",
    val repository: Int = 0,
    val company: String = "No Company",
    val followers: Int = 0,
    val following: Int = 0,
    val avatar: Int = 0,
    val avatarUrl: String = "",
    val isGetAPI: Boolean = true
)
