package com.example.critichub.ui.screens.search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.SearchOff
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.model.ContentType
import com.example.critichub.ui.components.ContentRowCard
import com.example.critichub.ui.components.EmptyView
import com.example.critichub.ui.components.ErrorView
import com.example.critichub.ui.components.LoadingView
import com.example.critichub.ui.components.SearchField
import com.example.critichub.viewmodel.SearchUiState
import com.example.critichub.viewmodel.SearchViewModel

private val ratingOptions = listOf(
    null to "همه امتیازها",
    7.0 to "۷ به بالا",
    8.0 to "۸ به بالا",
    9.0 to "۹ به بالا"
)

@Composable
fun SearchScreen(onOpenContent: (String) -> Unit) {
    val viewModel: SearchViewModel = viewModel(factory = SearchViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "جستجو",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 8.dp, bottom = 12.dp)
            )
            SearchField(
                value = viewModel.queryValue,
                onValueChange = viewModel::setQuery,
                placeholder = "جستجوی فیلم و سریال…"
            )
            Spacer(Modifier.height(12.dp))

            // فیلتر نوع محتوا
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = viewModel.typeValue == null,
                    onClick = { viewModel.setType(null) },
                    label = { Text("همه") }
                )
                FilterChip(
                    selected = viewModel.typeValue == ContentType.MOVIE,
                    onClick = { viewModel.setType(ContentType.MOVIE) },
                    label = { Text("فیلم") }
                )
                FilterChip(
                    selected = viewModel.typeValue == ContentType.SERIES,
                    onClick = { viewModel.setType(ContentType.SERIES) },
                    label = { Text("سریال") }
                )
            }
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                ratingOptions.forEach { (value, label) ->
                    FilterChip(
                        selected = viewModel.minRatingValue == value,
                        onClick = { viewModel.setMinRating(value) },
                        label = { Text(label) }
                    )
                }
            }
            Spacer(Modifier.height(4.dp))

            when (val state = uiState) {
                is SearchUiState.Loading ->
                    LoadingView(Modifier.fillMaxWidth(), label = "در حال جستجو…")

                is SearchUiState.Error -> ErrorView(
                    onRetry = { viewModel.retry() },
                    modifier = Modifier.fillMaxWidth()
                )

                is SearchUiState.Results -> {
                    if (state.items.isEmpty() && viewModel.queryValue.isNotBlank()) {
                        EmptyView(
                            title = "نتیجه‌ای یافت نشد",
                            subtitle = "عبارت دیگری را جستجو کنید یا فیلترها را تغییر دهید",
                            icon = Icons.Outlined.SearchOff
                        )
                    } else if (state.items.isEmpty()) {
                        EmptyView(
                            title = "جستجو کنید",
                            subtitle = "نام فارسی یا انگلیسی فیلم‌ها و سریال‌ها را وارد کنید",
                            icon = Icons.Outlined.SearchOff
                        )
                    } else {
                        LazyColumn(
                            contentPadding = PaddingValues(vertical = 10.dp),
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
