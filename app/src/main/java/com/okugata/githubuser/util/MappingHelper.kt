package com.okugata.githubuser.util

import android.database.Cursor
import com.okugata.githubuser.database.UserFavorite

object MappingHelper {
    fun mapCursorToArrayList(userFavoriteCursor: Cursor?): ArrayList<UserFavorite> {
        val users = ArrayList<UserFavorite>()

        userFavoriteCursor?.apply {
            while (moveToNext()) {
                users.add(
                    UserFavorite(
                        getLong(getColumnIndexOrThrow(UserFavorite.COLUMN_ID)),
                        getString(getColumnIndexOrThrow(UserFavorite.COLUMN_USERNAME)),
                        getString(getColumnIndexOrThrow(UserFavorite.COLUMN_NAME)),
                        getString(getColumnIndexOrThrow(UserFavorite.COLUMN_LOCATION)),
                        getInt(getColumnIndexOrThrow(UserFavorite.COLUMN_REPOSITORY)),
                        getString(getColumnIndexOrThrow(UserFavorite.COLUMN_COMPANY)),
                        getInt(getColumnIndexOrThrow(UserFavorite.COLUMN_FOLLOWERS)),
                        getInt(getColumnIndexOrThrow(UserFavorite.COLUMN_FOLLOWING)),
                        getInt(getColumnIndexOrThrow(UserFavorite.COLUMN_AVATAR)),
                        getString(getColumnIndexOrThrow(UserFavorite.COLUMN_AVATAR_URL)),
                        getInt(getColumnIndexOrThrow(UserFavorite.COLUMN_IS_GET_API)) > 0
                    )
                )
            }
        }
        return users
    }
}