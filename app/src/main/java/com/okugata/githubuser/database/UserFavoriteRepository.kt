package com.okugata.githubuser.database

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow


class UserFavoriteRepository(private val userFavoriteDao: UserFavoriteDao) {

    val allUser: Flow<List<UserFavorite>> = userFavoriteDao.getAllUser()

    @WorkerThread
    suspend fun insert(userFavorite: UserFavorite) {
        userFavoriteDao.insert(userFavorite)
    }

    @WorkerThread
    suspend fun delete(userFavorite: UserFavorite) {
        userFavoriteDao.delete(userFavorite)
    }
}