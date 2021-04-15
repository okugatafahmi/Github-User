package com.okugata.githubuser.contentprovider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.okugata.githubuser.database.GithubUserRoomDatabase
import com.okugata.githubuser.database.UserFavorite
import com.okugata.githubuser.database.UserFavoriteDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class UserFavoriteProvider : ContentProvider() {
    companion object {
        private const val TABLE_NAME = UserFavorite.TABLE_NAME
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        val CONTENT_URI: Uri = Uri.Builder().scheme("content")
            .authority(GithubUserRoomDatabase.AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()

        init {
            sUriMatcher.addURI(GithubUserRoomDatabase.AUTHORITY, TABLE_NAME, USER)
            sUriMatcher.addURI(GithubUserRoomDatabase.AUTHORITY, "$TABLE_NAME/#", USER_ID)
        }
    }

    private lateinit var database: GithubUserRoomDatabase
    private var userFavoriteDao: UserFavoriteDao? = null

    override fun onCreate(): Boolean {
        database = GithubUserRoomDatabase.getDatabase(context as Context, CoroutineScope(SupervisorJob()))
        userFavoriteDao = database.userFavoriteDao()
        return true
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        TODO("Implement this to handle query requests from clients.")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}