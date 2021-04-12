package com.okugata.githubuser.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserFavoriteDao {
    @Query("SELECT * FROM user_favorite ORDER BY username ASC")
    fun getAllUser(): Flow<List<UserFavorite>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userFavorite: UserFavorite)

    @Query("DELETE FROM user_favorite")
    suspend fun deleteAll()
}