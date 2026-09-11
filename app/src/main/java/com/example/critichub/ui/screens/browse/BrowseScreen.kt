package com.example.critichub.ui.screens.browse

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.MovieFilter
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.data.repository.SortOption
import com.example.critichub.model.ContentType
import com.example.critichub.model.Genre
import com.example.critichub.ui.components.ContentRowCard
import com.example.critichub.ui.components.CriticHubTopBar
import com.example.critichub.ui.components.EmptyView
import com.example.critichub.ui.components.ErrorView
import com.example.critichub.ui.components.LoadingView
import com.example.critichub.viewmodel.BrowseUiState
import com.example.critichub.viewmodel.BrowseViewModel

private val ratingFilters = listOf(
    null to "همه",
    7.0 to "۷+",
    8.0 to "۸+",
    9.0 to "۹+"
)

/** صفحهٔ مرور و فیلتر (فیلم/سریال/ژانر/امتیاز/مرتب‌سازی). */
@Composable
fun BrowseScreen(
    onBack: () -> Unit,
    onOpenContent: (String) -> Unit
) {
    val viewModel: BrowseViewModel = viewModel(factory = BrowseViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var genres by remember { mutableStateOf<List<Genre>>(emptyList()) }
    LaunchedEffect(uiState) {
        val content = uiState as? BrowseUiState.Content
        if (content != null && genres.isEmpty()) genres = content.genres
    }

    val selectedGenre by viewModel.selectedGenreId.collectAsStateWithLifecycle()
    val selectedType by viewModel.selectedType.collectAsStateWithLifecycle()
    val selectedRating by viewModel.selectedMinRating.collectAsStateWithLifecycle()
    val selectedSort by viewModel.selectedSort.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        topBar = {
            CriticHubTopBar(
                title = selectedGenre?.let { id -> genres.firstOrNull { it.id == id }?.nameFa }
                    ?: "مرور و فیلتر",
                onBack = onBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // فیلترها
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(Modifier.height(6.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedType == null,
                        onClick = { viewModel.setType(null) },
                        label = { Text("همه") }
                    )
                    FilterChip(
                        selected = selectedType == ContentType.MOVIE,
                        onClick = { viewModel.setType(ContentType.MOVIE) },
                        label = { Text("فیلم‌ها") }
                    )
                    FilterChip(
                        selected = selectedType == ContentType.SERIES,
                        onClick = { viewModel.setType(ContentType.SERIES) },
                        label = { Text("سریال‌ها") }
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedGenre == null,
                        onClick = { viewModel.setGenre(null) },
                        label = { Text("همهٔ ژانرها") }
                    )
                    genres.forEach { genre ->
                        FilterChip(
                            selected = selectedGenre == genre.id,
                            onClick = { viewModel.setGenre(genre.id) },
                            label = { Text(genre.nameFa) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ratingFilters.forEach { (value, label) ->
                        FilterChip(
                            selected = selectedRating == value,
                            onClick = { viewModel.setMinRating(value) },
                            label = { Text("امتیاز $label") }
                        )
                    }
                    Spacer(Modifier.width(4.dp))
                    FilterChip(
                        selected = selectedSort == SortOption.RATING,
                        onClick = { viewModel.setSort(SortOption.RATING) },
                        label = { Text("بیشترین امتیاز") }
                    )
                    FilterChip(
                        selected = selectedSort == SortOption.NEWEST,
                        onClick = { viewModel.setSort(SortOption.NEWEST) },
                        label = { Text("جدیدترین") }
                    )
                }
                Spacer(Modifier.height(4.dp))
            }

            when (val state = uiState) {
                is BrowseUiState.Loading ->
                    LoadingView(Modifier.fillMaxWidth())

                is BrowseUiState.Error ->
                    ErrorView(onRetry = { viewModel.reload() })

                is BrowseUiState.Content -> {
                    if (state.items.isEmpty()) {
                        EmptyView(
                            title = "موردی با این فیلترها یافت نشد",
                            subtitle = "فیلترها را تغییر دهید",
                            icon = Icons.Outlined.MovieFilter
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            items(state.items, key = { it.id }) { item ->
                                ContentRowCard(
                                    item = item,
                                    onClick = { onOpenContent(item.id) },
                                    isFavorite = item.id in state.favoriteIds,
                                    onToggleFavorite = { viewModel.toggleFavorite(item) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
