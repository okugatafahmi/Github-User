package com.okugata.githubuser.database

import android.content.ContentValues
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserFavorite.TABLE_NAME)
data class UserFavorite(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = COLUMN_ID) val id: Long = 0,
    @ColumnInfo(name = COLUMN_USERNAME) val username: String,
    @ColumnInfo(name = COLUMN_NAME) val name: String = "",
    @ColumnInfo(name = COLUMN_LOCATION) val location: String = "No Location",
    @ColumnInfo(name = COLUMN_REPOSITORY) val repository: Int = 0,
    @ColumnInfo(name = COLUMN_COMPANY) val company: String = "No Company",
    @ColumnInfo(name = COLUMN_FOLLOWERS) val followers: Int = 0,
    @ColumnInfo(name = COLUMN_FOLLOWING) val following: Int = 0,
    @ColumnInfo(name = COLUMN_AVATAR) val avatar: Int = 0,
    @ColumnInfo(name = COLUMN_AVATAR_URL) val avatarUrl: String = "",
    @ColumnInfo(name = COLUMN_IS_GET_API) val isGetAPI: Boolean = true
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

        fun fromContentValues(contentValues: ContentValues?, id: Long? = null): UserFavorite {
            if (contentValues == null) return UserFavorite(username = "")
            // assume that there are values
            return UserFavorite(
                id ?: contentValues.getAsLong(COLUMN_ID) ?: 0,
                contentValues.getAsString(COLUMN_USERNAME),
                contentValues.getAsString(COLUMN_NAME),
                contentValues.getAsString(COLUMN_LOCATION),
                contentValues.getAsInteger(COLUMN_REPOSITORY),
                contentValues.getAsString(COLUMN_COMPANY),
                contentValues.getAsInteger(COLUMN_FOLLOWERS),
                contentValues.getAsInteger(COLUMN_FOLLOWING),
                contentValues.getAsInteger(COLUMN_AVATAR),
                contentValues.getAsString(COLUMN_AVATAR_URL),
                contentValues.getAsBoolean(COLUMN_IS_GET_API)
            )
        }
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
