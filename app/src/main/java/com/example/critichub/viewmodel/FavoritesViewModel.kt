package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.FavoritesRepository
import com.example.critichub.model.ContentType
import com.example.critichub.model.Favorite
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val movies: List<Favorite> = emptyList(),
    val series: List<Favorite> = emptyList()
)

class FavoritesViewModel(
    favoritesRepository: FavoritesRepository
) : ViewModel() {

    val uiState: StateFlow<FavoritesUiState> = favoritesRepository.favorites
        .map { favorites ->
            FavoritesUiState(
                movies = favorites.filter { it.type == ContentType.MOVIE },
                series = favorites.filter { it.type == ContentType.SERIES }
            )
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), FavoritesUiState())

    private val repo = favoritesRepository

    fun remove(favorite: Favorite) {
        viewModelScope.launch { runCatching { repo.remove(favorite.contentId) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                FavoritesViewModel(app.favoritesRepository)
            }
        }
    }
}
