package com.okugata.githubuser.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = [UserFavorite::class], version = 1, exportSchema = false)
abstract class GithubUserRoomDatabase : RoomDatabase() {

    abstract fun userFavoriteDao(): UserFavoriteDao

    companion object {
        const val AUTHORITY = "com.okugata.githubuser"

        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: GithubUserRoomDatabase? = null

        fun getDatabase(context: Context): GithubUserRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GithubUserRoomDatabase::class.java,
                    "github_user_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}