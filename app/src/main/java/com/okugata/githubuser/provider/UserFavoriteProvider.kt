package com.okugata.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.okugata.githubuser.database.GithubUserRoomDatabase
import com.okugata.githubuser.database.GithubUserRoomDatabase.Companion.AUTHORITY
import com.okugata.githubuser.database.UserFavorite
import com.okugata.githubuser.database.UserFavoriteDao

class UserFavoriteProvider : ContentProvider() {
    companion object {
        private const val USER_FAVORITE_TABLE = UserFavorite.TABLE_NAME
        private const val CODE_USER = 1
        private const val CODE_USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        val USER_FAVORITE_URI: Uri = Uri.Builder().scheme("content")
            .authority(AUTHORITY)
            .appendPath(USER_FAVORITE_TABLE)
            .build()

        init {
            sUriMatcher.addURI(AUTHORITY, USER_FAVORITE_TABLE, CODE_USER)
            sUriMatcher.addURI(AUTHORITY, "$USER_FAVORITE_TABLE/#", CODE_USER_ID)
        }
    }
    private lateinit var dao: UserFavoriteDao

    override fun onCreate(): Boolean {
        dao = GithubUserRoomDatabase.getDatabase(context as Context).userFavoriteDao()
        return true
    }

    override fun getType(uri: Uri): String? {
        return null
    }
    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when(sUriMatcher.match(uri)) {
            CODE_USER -> dao.getAllUserCursor()
            CODE_USER_ID -> dao.getUserByIdCursor(uri.lastPathSegment.toString().toLong())
            else -> null
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val added = when (CODE_USER) {
            sUriMatcher.match(uri) -> dao.insert(UserFavorite.fromContentValues(values))
            else -> 0
        }
        context?.contentResolver?.notifyChange(USER_FAVORITE_URI, null)
        return Uri.parse("$USER_FAVORITE_URI/$added")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val updated = when (CODE_USER_ID) {
            sUriMatcher.match(uri) -> dao.update(UserFavorite.fromContentValues(values, uri.lastPathSegment.toString().toLong()))
            else -> 0
        }
        context?.contentResolver?.notifyChange(USER_FAVORITE_URI, null)
        return updated
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted = when (CODE_USER_ID) {
            sUriMatcher.match(uri) -> dao.delete(uri.lastPathSegment.toString().toLong())
            else -> 0
        }

        context?.contentResolver?.notifyChange(USER_FAVORITE_URI, null)

        return deleted
    }
}