package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.CatalogRepository
import com.example.critichub.data.repository.FavoritesRepository
import com.example.critichub.data.repository.HomeSections
import com.example.critichub.model.CatalogItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Content(
        val sections: HomeSections,
        val favoriteIds: Set<String>
    ) : HomeUiState

    data object Error : HomeUiState
}

class HomeViewModel(
    private val catalogRepository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val sectionsState = MutableStateFlow<HomeSections?>(null)
    private val loading = MutableStateFlow(true)
    private val error = MutableStateFlow(false)

    val uiState: StateFlow<HomeUiState> = combine(
        sectionsState, loading, error, favoritesRepository.favorites
    ) { sections, isLoading, hasError, favorites ->
        when {
            hasError -> HomeUiState.Error
            isLoading || sections == null -> HomeUiState.Loading
            else -> HomeUiState.Content(
                sections = sections,
                favoriteIds = favorites.map { it.contentId }.toSet()
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), HomeUiState.Loading)

    init {
        load()
    }

    fun load() {
        loading.value = true
        error.value = false
        viewModelScope.launch {
            runCatching { catalogRepository.getHomeSections() }
                .onSuccess {
                    sectionsState.value = it
                    loading.value = false
                }
                .onFailure {
                    loading.value = false
                    error.value = true
                }
        }
    }

    fun toggleFavorite(item: CatalogItem) {
        viewModelScope.launch { runCatching { favoritesRepository.toggle(item) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                HomeViewModel(app.catalogRepository, app.favoritesRepository)
            }
        }
    }
}
