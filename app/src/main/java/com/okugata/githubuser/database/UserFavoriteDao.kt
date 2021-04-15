package com.okugata.githubuser.database

import android.database.Cursor
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM ${UserFavorite.TABLE_NAME} ORDER BY username ASC")
    fun getAllUser(): Flow<List<UserFavorite>>

    @Query("SELECT * FROM ${UserFavorite.TABLE_NAME} ORDER BY username ASC")
    fun getAllUserCursor(): Cursor

    @Query("SELECT * FROM ${UserFavorite.TABLE_NAME} WHERE id = :id")
    fun getUserByIdCursor(id: Long): Cursor

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(userFavorite: UserFavorite): Long

    @Update
    fun update(userFavorite: UserFavorite): Int

    @Query("DELETE FROM ${UserFavorite.TABLE_NAME}")
    suspend fun deleteAll()

//    @Delete(entity = UserFavorite::class)
//    fun delete(id: Long): Int
}