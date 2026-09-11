package com.example.critichub.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.Genre
import com.example.critichub.ui.components.EmptyView
import com.example.critichub.ui.components.ErrorView
import com.example.critichub.ui.components.LoadingView
import com.example.critichub.ui.components.PosterCard
import com.example.critichub.ui.components.SectionHeader
import com.example.critichub.viewmodel.HomeUiState
import com.example.critichub.viewmodel.HomeViewModel

/** صفحهٔ اصلی (اکتشاف محتوا). */
@Composable
fun HomeScreen(
    onOpenContent: (String) -> Unit,
    onOpenBrowse: (genreId: String?, type: ContentType?) -> Unit,
    onOpenSearch: () -> Unit
) {
    val viewModel: HomeViewModel = viewModel(factory = HomeViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = androidx.compose.foundation.layout.WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { padding ->
        when (val state = uiState) {
            is HomeUiState.Loading -> LoadingView(Modifier.padding(padding))
            is HomeUiState.Error -> ErrorView(
                onRetry = viewModel::load,
                modifier = Modifier.padding(padding)
            )
            is HomeUiState.Content -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(bottom = 20.dp)
            ) {
                item(key = "header") { HomeHeader(onOpenSearch) }

                if (state.sections.popularMovies.isEmpty() &&
                    state.sections.popularSeries.isEmpty() &&
                    state.sections.newReleases.isEmpty()
                ) {
                    item {
                        EmptyView(
                            title = "موردی برای نمایش وجود ندارد",
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                sectionRow(
                    title = "فیلم‌های محبوب",
                    items = state.sections.popularMovies,
                    favoriteIds = state.favoriteIds,
                    onOpenContent = onOpenContent,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onSeeAll = { onOpenBrowse(null, ContentType.MOVIE) }
                )

                sectionRow(
                    title = "سریال‌های محبوب",
                    items = state.sections.popularSeries,
                    favoriteIds = state.favoriteIds,
                    onOpenContent = onOpenContent,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onSeeAll = { onOpenBrowse(null, ContentType.SERIES) }
                )

                sectionRow(
                    title = "تازه‌ها",
                    items = state.sections.newReleases,
                    favoriteIds = state.favoriteIds,
                    onOpenContent = onOpenContent,
                    onToggleFavorite = viewModel::toggleFavorite,
                    onSeeAll = null
                )

                item(key = "genres-header") {
                    SectionHeader(title = "ژانرها")
                }
                item(key = "genres-row") {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
                    ) {
                        items(state.sections.genres, key = { it.id }) { genre ->
                            GenreChip(genre = genre) { onOpenBrowse(genre.id, null) }
                        }
                    }
                }
                item(key = "bottom-note") {
                    Text(
                        text = "این نسخه نمونه با دادهٔ محلی کار می‌کند؛ پس از اتصال API، بخش‌ها به‌روزرسانی می‌شوند.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 18.dp)
                    )
                }
            }
        }
    }
}

private fun LazyListScope.sectionRow(
    title: String,
    items: List<CatalogItem>,
    favoriteIds: Set<String>,
    onOpenContent: (String) -> Unit,
    onToggleFavorite: (CatalogItem) -> Unit,
    onSeeAll: (() -> Unit)?
) {
    if (items.isEmpty()) return
    item(key = "header-$title") {
        SectionHeader(title = title, onSeeAll = onSeeAll)
    }
    item(key = "row-$title") {
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            items(items, key = { it.id }) { item ->
                PosterCard(
                    item = item,
                    onClick = { onOpenContent(item.id) },
                    isFavorite = item.id in favoriteIds,
                    onToggleFavorite = { onToggleFavorite(item) }
                )
            }
        }
    }
}

@Composable
private fun HomeHeader(onOpenSearch: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(start = 20.dp, end = 8.dp, top = 8.dp, bottom = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    brush = Brush.linearGradient(listOf(Color(0xFF2F6BFF), Color(0xFF1D4ED8))),
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "کریتیک‌هاب",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "دنیای فیلم و سریال",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        IconButton(onClick = onOpenSearch) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "جستجو",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun GenreChip(genre: Genre, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Text(
            text = genre.nameFa,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 9.dp)
        )
    }
}
