package com.okugata.githubuser.database

import android.database.Cursor
import androidx.room.*
import com.okugata.githubuser.database.UserFavorite.Companion.COLUMN_ID
import com.okugata.githubuser.database.UserFavorite.Companion.COLUMN_USERNAME
import com.okugata.githubuser.database.UserFavorite.Companion.TABLE_NAME
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_USERNAME ASC")
    fun getAllUser(): Flow<List<UserFavorite>>

    @Query("SELECT * FROM $TABLE_NAME ORDER BY $COLUMN_USERNAME ASC")
    fun getAllUserCursor(): Cursor

    @Query("SELECT * FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun getUserByIdCursor(id: Long): Cursor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userFavorite: UserFavorite): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSuspend(userFavorite: UserFavorite)

    @Update
    fun update(userFavorite: UserFavorite): Int

    @Query("DELETE FROM $TABLE_NAME")
    suspend fun deleteAll()

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    fun delete(id: Long): Int

    @Query("DELETE FROM $TABLE_NAME WHERE $COLUMN_ID = :id")
    suspend fun deleteSuspend(id: Long): Int
}