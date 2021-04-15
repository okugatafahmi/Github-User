package com.okugata.githubuser.database

import android.content.ContentValues
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserFavorite.TABLE_NAME)
data class UserFavorite(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
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
        fun fromContentValues(contentValues: ContentValues?, id: Long? = null): UserFavorite {
            if (contentValues == null) return UserFavorite(username = "")
            // assume that there are values
            return UserFavorite(
                id ?: contentValues.getAsLong("id") ?: 0,
                contentValues.getAsString("username"),
                contentValues.getAsString("name"),
                contentValues.getAsString("location"),
                contentValues.getAsInteger("repository"),
                contentValues.getAsString("company"),
                contentValues.getAsInteger("followers"),
                contentValues.getAsInteger("following"),
                contentValues.getAsInteger("avatar"),
                contentValues.getAsString("avatarUrl"),
                contentValues.getAsBoolean("isGetAPI")
            )
        }
    }
}
