package com.okugata.githubuser.database

import androidx.lifecycle.*
import kotlinx.coroutines.launch

class UserFavoriteViewModel(private val repository: UserFavoriteRepository) : ViewModel() {

    // Using LiveData and caching what allWords returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allUser: LiveData<List<UserFavorite>> = repository.allUser.asLiveData()

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(user: UserFavorite) = viewModelScope.launch {
        repository.insert(user)
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