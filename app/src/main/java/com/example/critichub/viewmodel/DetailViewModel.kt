package com.example.critichub.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.CatalogRepository
import com.example.critichub.data.repository.FavoritesRepository
import com.example.critichub.model.CatalogItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class DetailUiState(
    val loading: Boolean = true,
    val error: Boolean = false,
    val item: CatalogItem? = null,
    val isFavorite: Boolean = false
)

class DetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val catalogRepository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val contentId: String = checkNotNull(savedStateHandle["contentId"])

    private sealed interface LoadState {
        data object Loading : LoadState
        data class Ready(val item: CatalogItem) : LoadState
        data object Error : LoadState
    }

    private val loadState = MutableStateFlow<LoadState>(LoadState.Loading)

    val uiState: StateFlow<DetailUiState> = combine(loadState, favoritesRepository.favorites) { state, favorites ->
        when (state) {
            is LoadState.Loading -> DetailUiState(loading = true)
            is LoadState.Error -> DetailUiState(loading = false, error = true)
            is LoadState.Ready -> DetailUiState(
                loading = false,
                item = state.item,
                isFavorite = favorites.any { it.contentId == state.item.id }
            )
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DetailUiState())

    init {
        load()
    }

    fun load() {
        loadState.value = LoadState.Loading
        viewModelScope.launch {
            runCatching { catalogRepository.getItemById(contentId) }
                .onSuccess { item ->
                    loadState.value =
                        if (item != null) LoadState.Ready(item) else LoadState.Error
                }
                .onFailure { loadState.value = LoadState.Error }
        }
    }

    fun toggleFavorite(item: CatalogItem) {
        viewModelScope.launch { runCatching { favoritesRepository.toggle(item) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                DetailViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    catalogRepository = app.catalogRepository,
                    favoritesRepository = app.favoritesRepository
                )
            }
        }
    }
}
