package com.example.critichub.ui.screens.favorites

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.model.Favorite
import com.example.critichub.ui.components.EmptyView
import com.example.critichub.ui.components.PosterArt
import com.example.critichub.ui.components.StarRating
import com.example.critichub.ui.theme.FavoriteRed
import com.example.critichub.util.fa
import com.example.critichub.viewmodel.FavoritesViewModel

/** صفحهٔ علاقه‌مندی‌ها با دو زبانهٔ فیلم‌ها و سریال‌ها. */
@Composable
fun FavoritesScreen(
    onOpenContent: (String) -> Unit
) {
    val viewModel: FavoritesViewModel = viewModel(factory = FavoritesViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var tab by rememberSaveable { mutableIntStateOf(0) }
    val movies = uiState.movies
    val series = uiState.series

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding()
        ) {
            Text(
                text = "علاقه‌مندی‌ها",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 8.dp)
            )
            TabRow(selectedTabIndex = tab) {
                Tab(
                    selected = tab == 0,
                    onClick = { tab = 0 },
                    text = { Text("فیلم‌ها (${movies.size.fa()})") }
                )
                Tab(
                    selected = tab == 1,
                    onClick = { tab = 1 },
                    text = { Text("سریال‌ها (${series.size.fa()})") }
                )
            }
            val current = if (tab == 0) movies else series
            if (current.isEmpty()) {
                EmptyView(
                    title = if (tab == 0)
                        "هنوز فیلمی به علاقه‌مندی‌ها اضافه نکرده‌اید"
                    else
                        "هنوز سریالی به علاقه‌مندی‌ها اضافه نکرده‌اید",
                    subtitle = "با لمس آیکون قلب روی کارت‌ها، آیتم‌ها این‌جا ذخیره می‌شوند"
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(10.dp)
                ) {
                    items(current, key = { it.contentId }) { favorite ->
                        FavoriteRowCard(
                            favorite = favorite,
                            onClick = { onOpenContent(favorite.contentId) },
                            onRemove = { viewModel.remove(favorite) }
                        )
                    }
                }
            }
        }
    }
}

/** کارت ردیفی یک آیتم علاقه‌مندیشده. */
@Composable
fun FavoriteRowCard(
    favorite: Favorite,
    onClick: () -> Unit,
    onRemove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PosterArt(
                id = favorite.contentId,
                type = favorite.type,
                modifier = Modifier.size(width = 58.dp, height = 84.dp),
                shape = RoundedCornerShape(12.dp),
                iconSize = 24.dp
            )
            Spacer(Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = favorite.titleFa,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (favorite.titleEn.isNotBlank()) {
                    Text(
                        text = favorite.titleEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    StarRating(rating = favorite.rating, starSize = 13)
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = favorite.year.fa(),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            IconButton(onClick = onRemove) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "حذف از علاقه‌مندی‌ها",
                    tint = FavoriteRed
                )
            }
        }
    }
}
