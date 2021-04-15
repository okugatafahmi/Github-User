package com.okugata.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class GithubUserRoomDatabase : RoomDatabase() {

    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        const val AUTHORITY = "com.okugata.github_user"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: GithubUserRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): GithubUserRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserRoomDatabase::class.java,
                    "github_user_database"
                ).addCallback(WordDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }

        private class WordDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.userFavoriteDao())
                    }
                }
            }

            suspend fun populateDatabase(userFavoriteDao: UserFavoriteDao) {
                // Delete all content.
                userFavoriteDao.deleteAll()

                // Add sample user favorite.
                userFavoriteDao.insert(
                    UserFavorite(
                        username = "okugatafahmi",
                        name = "Okugata Fahmi",
                        avatarUrl = "https://avatars.githubusercontent.com/u/47854810?v=4"
                    )
                )
            }
        }
    }
}