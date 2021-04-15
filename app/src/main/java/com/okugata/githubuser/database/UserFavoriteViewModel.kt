package com.okugata.githubuser.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val dao: UserFavoriteDao) : ViewModel() {

    val allUser: LiveData<List<UserFavorite>> = dao.getAllUser().asLiveData()

    fun insert(user: UserFavorite) = viewModelScope.launch {
        dao.insertSuspend(user)
    }

    fun delete(id: Long) = viewModelScope.launch {
        dao.deleteSuspend(id)
    }
}

class UserFavoriteViewModelFactory(private val dao: UserFavoriteDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFavoriteViewModel(dao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}