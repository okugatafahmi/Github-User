package com.okugata.githubuser

import android.app.Application
import com.okugata.githubuser.database.GithubUserRoomDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

class GithubUserApplication : Application() {
    // No need to cancel this scope as it'll be torn down with the process
    private val applicationScope = CoroutineScope(SupervisorJob())

    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { GithubUserRoomDatabase.getDatabase(this, applicationScope) }
    val userFavoriteDao by lazy { database.userFavoriteDao() }
}