package com.okugata.githubuser.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM ${UserFavorite.TABLE_NAME} ORDER BY username ASC")
    fun getAllUser(): Flow<List<UserFavorite>>

    @Query("DELETE FROM ${UserFavorite.TABLE_NAME}")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userFavorite: UserFavorite)

    @Delete
    suspend fun delete(userFavorite: UserFavorite)
}