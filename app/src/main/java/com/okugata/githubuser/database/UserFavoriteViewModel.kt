package com.okugata.githubuser.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val repository: UserFavoriteRepository) : ViewModel() {

    val allUser: LiveData<List<UserFavorite>> = repository.allUser.asLiveData()

    fun insert(user: UserFavorite) = viewModelScope.launch {
        repository.insert(user)
    }

    fun delete(user: UserFavorite) = viewModelScope.launch {
        repository.delete(user)
    }
}

class UserFavoriteViewModelFactory(private val repository: UserFavoriteRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserFavoriteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserFavoriteViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}