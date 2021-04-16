package com.okugata.githubuserfavorite.database

import android.content.ContentValues

data class UserFavorite(
    val id: Long = 0,
    val username: String,
    val name: String = "",
    val location: String = "No Location",
    val repository: Int = 0,
    val company: String = "No Company",
    val followers: Int = 0,
    val following: Int = 0,
    val avatar: Int = 0,
    val avatarUrl: String = "",
    val isGetAPI: Boolean = true
) {
    companion object {
        const val TABLE_NAME = "user_favorite"
        const val COLUMN_ID = "id"
        const val COLUMN_USERNAME = "username"
        const val COLUMN_NAME = "name"
        const val COLUMN_LOCATION = "location"
        const val COLUMN_REPOSITORY = "repository"
        const val COLUMN_COMPANY = "company"
        const val COLUMN_FOLLOWERS = "followers"
        const val COLUMN_FOLLOWING = "following"
        const val COLUMN_AVATAR = "avatar"
        const val COLUMN_AVATAR_URL = "avatar_url"
        const val COLUMN_IS_GET_API = "is_get_api"
    }

    fun toContentValues(): ContentValues {
        return ContentValues().apply {
            put(COLUMN_ID, id)
            put(COLUMN_USERNAME, username)
            put(COLUMN_NAME, name)
            put(COLUMN_LOCATION, location)
            put(COLUMN_REPOSITORY, repository)
            put(COLUMN_COMPANY, company)
            put(COLUMN_FOLLOWERS, followers)
            put(COLUMN_FOLLOWING, following)
            put(COLUMN_AVATAR, avatar)
            put(COLUMN_AVATAR_URL, avatarUrl)
            put(COLUMN_IS_GET_API, isGetAPI)
        }
    }
}
