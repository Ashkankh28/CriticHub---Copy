package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.AuthRepository
import com.example.critichub.data.repository.FavoritesRepository
import com.example.critichub.model.Favorite
import com.example.critichub.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ProfileUiState(
    val user: User? = null,
    val favorites: List<Favorite> = emptyList(),
    val loggingOut: Boolean = false
)

class ProfileViewModel(
    authRepository: AuthRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val auth = authRepository

    private val loggingOut = MutableStateFlow(false)

    val uiState: StateFlow<ProfileUiState> = combine(
        authRepository.currentUser, favoritesRepository.favorites, loggingOut
    ) { user, favorites, isLoggingOut ->
        ProfileUiState(user = user, favorites = favorites, loggingOut = isLoggingOut)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ProfileUiState())

    fun logout() {
        loggingOut.value = true
        viewModelScope.launch {
            runCatching { auth.logout() }
            loggingOut.value = false
        }
    }

    fun removeFavorite(contentId: String) {
        viewModelScope.launch { runCatching { favoritesRepository.remove(contentId) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                ProfileViewModel(app.authRepository, app.favoritesRepository)
            }
        }
    }
}
