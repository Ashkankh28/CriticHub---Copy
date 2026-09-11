package com.example.critichub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.critichub.data.repository.CatalogRepository
import com.example.critichub.data.repository.FavoritesRepository
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Results(val items: List<CatalogItem>, val favoriteIds: Set<String>) : SearchUiState
    data object Error : SearchUiState
}

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
class SearchViewModel(
    private val catalogRepository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val query = MutableStateFlow("")
    private val typeFilter = MutableStateFlow<ContentType?>(null)
    private val minRating = MutableStateFlow<Double?>(null)
    private val retryTrigger = MutableStateFlow(0)

    val queryValue: String get() = query.value
    val typeValue: ContentType? get() = typeFilter.value
    val minRatingValue: Double? get() = minRating.value

    val uiState: StateFlow<SearchUiState> =
        combine(query.debounce(250), typeFilter, minRating, retryTrigger) { q, t, r, _ ->
            Triple(q.trim(), t, r)
        }
            .flatMapLatest { (q, t, r) ->
                flow {
                    emit(SearchUiState.Loading)
                    if (q.isEmpty()) {
                        emit(SearchUiState.Results(emptyList(), emptySet()))
                        return@flow
                    }
                    val result = try {
                        catalogRepository.search(query = q, type = t, minRating = r)
                    } catch (_: Exception) {
                        emit(SearchUiState.Error)
                        return@flow
                    }
                    emit(SearchUiState.Results(result, emptySet()))
                }
            }
            .combine(favoritesRepository.favorites) { state, favorites ->
                when (state) {
                    is SearchUiState.Results -> SearchUiState.Results(
                        items = state.items,
                        favoriteIds = favorites.map { it.contentId }.toSet()
                    )
                    else -> state
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SearchUiState.Loading)

    fun setQuery(value: String) {
        if (query.value != value) query.value = value
    }

    fun setType(type: ContentType?) {
        if (typeFilter.value != type) typeFilter.value = type
    }

    fun setMinRating(rating: Double?) {
        if (minRating.value != rating) minRating.value = rating
    }

    fun retry() {
        retryTrigger.value++
    }

    fun toggleFavorite(item: CatalogItem) {
        viewModelScope.launch { runCatching { favoritesRepository.toggle(item) } }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val app = appContainer
                SearchViewModel(app.catalogRepository, app.favoritesRepository)
            }
        }
    }
}
