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
import com.example.critichub.data.repository.SortOption
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.Genre
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface BrowseUiState {
    data object Loading : BrowseUiState
    data class Content(
        val items: List<CatalogItem>,
        val favoriteIds: Set<String>,
        val genres: List<Genre>
    ) : BrowseUiState

    data object Error : BrowseUiState
}

@OptIn(ExperimentalCoroutinesApi::class)
class BrowseViewModel(
    savedStateHandle: SavedStateHandle,
    private val catalogRepository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private data class BrowseFilters(
        val type: ContentType? = null,
        val genreId: String? = null,
        val minRating: Double? = null,
        val sort: SortOption = SortOption.RATING,
        val version: Int = 0
    )

    private val filters = MutableStateFlow(
        BrowseFilters(
            type = ContentType.entries.firstOrNull {
                it.name == savedStateHandle.get<String>("type")?.uppercase()
            },
            genreId = savedStateHandle.get<String>("genreId")?.takeIf { it != "all" }
        )
    )

    val uiState: StateFlow<BrowseUiState> = filters
        .flatMapLatest { f -> load(f) }
        .combine(favoritesRepository.favorites) { state, favorites ->
            if (state is BrowseUiState.Content) {
                state.copy(favoriteIds = favorites.map { it.contentId }.toSet())
            } else state
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), BrowseUiState.Loading)

    val selectedGenreId: StateFlow<String?> =
        filters.map { it.genreId }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), filters.value.genreId)
    val selectedType: StateFlow<ContentType?> =
        filters.map { it.type }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), filters.value.type)
    val selectedMinRating: StateFlow<Double?> =
        filters.map { it.minRating }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), filters.value.minRating)
    val selectedSort: StateFlow<SortOption> =
        filters.map { it.sort }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), filters.value.sort)

    private fun load(f: BrowseFilters): kotlinx.coroutines.flow.Flow<BrowseUiState> = flow {
        emit(BrowseUiState.Loading)
        val content = try {
            val genres = catalogRepository.getGenres()
            val items = catalogRepository.filter(
                type = f.type,
                genreId = f.genreId,
                minRating = f.minRating,
                sort = f.sort
            )
            BrowseUiState.Content(items, emptySet(), genres)
        } catch (_: Exception) {
            emit(BrowseUiState.Error)
            return@flow
        }
        emit(content)
    }

    fun setType(value: ContentType?) = filters.update { it.copy(type = value, version = it.version + 1) }

    fun setGenre(id: String?) = filters.update { it.copy(genreId = id, version = it.version + 1) }

    fun setMinRating(value: Double?) = filters.update { it.copy(minRating = value, version = it.version + 1) }

    fun setSort(value: SortOption) = filters.update { it.copy(sort = value, version = it.version + 1) }

    fun reload() = filters.update { it.copy(version = it.version + 1) }

    fun toggleFavorite(item: CatalogItem) {
        viewModelScope.launch { runCatching { favoritesRepository.toggle(item) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                BrowseViewModel(
                    savedStateHandle = createSavedStateHandle(),
                    catalogRepository = app.catalogRepository,
                    favoritesRepository = app.favoritesRepository
                )
            }
        }
    }
}
