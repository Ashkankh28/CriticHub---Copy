package com.example.critichub.ui.screens.detail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.model.Episode
import com.example.critichub.model.Season
import com.example.critichub.ui.components.AvatarCircle
import com.example.critichub.ui.components.CriticReviewCard
import com.example.critichub.ui.components.ErrorView
import com.example.critichub.ui.components.LoadingView
import com.example.critichub.ui.components.PersonCard
import com.example.critichub.ui.components.PosterArt
import com.example.critichub.ui.components.UserCommentCard
import com.example.critichub.ui.components.posterColors
import com.example.critichub.ui.theme.FavoriteRed
import com.example.critichub.ui.theme.RatingGold
import com.example.critichub.util.fa
import com.example.critichub.util.faRating
import com.example.critichub.util.faRuntime
import com.example.critichub.util.faVoteCount
import com.example.critichub.util.toFaDigits
import com.example.critichub.viewmodel.DetailUiState
import com.example.critichub.viewmodel.DetailViewModel

/** صفحهٔ جزئیات فیلم/سریال (شامل فصل‌ها و قسمت‌ها برای سریال‌ها). */
@Composable
fun DetailScreen(onBack: () -> Unit) {
    val viewModel: DetailViewModel = viewModel(factory = DetailViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when {
        uiState.loading -> LoadingView()
        uiState.error -> {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 4.dp, vertical = 4.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت"
                        )
                    }
                }
                ErrorView(onRetry = viewModel::load, modifier = Modifier.weight(1f))
            }
        }
        uiState.item != null -> {
            DetailContent(
                item = uiState.item!!,
                isFavorite = uiState.isFavorite,
                onBack = onBack,
                onToggleFavorite = { viewModel.toggleFavorite(uiState.item!!) }
            )
        }
    }
}

@Composable
private fun DetailContent(
    item: CatalogItem,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item(key = "hero") { DetailHero(item, isFavorite, onBack, onToggleFavorite) }

        item(key = "synopsis") {
            SectionTitle("خلاصه داستان")
            Text(
                text = item.synopsis,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item(key = "crew") { CrewSection(item) }

        if (item.isSeries && item.seasons.isNotEmpty()) {
            item(key = "seasons-title") { SectionTitle("فصل‌ها و قسمت‌ها") }
            items(item.seasons, key = { "season-${it.number}" }) { season ->
                SeasonCard(season = season, modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp))
            }
        }

        if (item.criticReviews.isNotEmpty()) {
            item(key = "reviews-title") { SectionTitle("نقدهای منتقدان") }
            items(item.criticReviews, key = { "review-${it.headline}" }) { review ->
                CriticReviewCard(
                    review = review,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }
        }

        if (item.userComments.isNotEmpty()) {
            item(key = "comments-title") {
                SectionTitle("نظرات کاربران")
            }
            item(key = "comments-note") {
                Text(
                    text = "نظرات و نقدها صرفاً جنبهٔ نمایشی دارند و در آینده از API دریافت می‌شوند.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 2.dp)
                )
            }
            items(item.userComments, key = { "comment-${it.author.nameFa}-${it.body.hashCode()}" }) { comment ->
                UserCommentCard(
                    comment = comment,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        }
    }
}

@Composable
private fun DetailHero(
    item: CatalogItem,
    isFavorite: Boolean,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    val (top, bottom) = remember(item.id) { posterColors(item.id) }
    val bg = MaterialTheme.colorScheme.background
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Brush.verticalGradient(listOf(top, bottom, bg)))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.Black.copy(alpha = 0.35f),
                    modifier = Modifier.size(40.dp)
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "بازگشت",
                            tint = Color.White
                        )
                    }
                }
            }
            Row(
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp),
                verticalAlignment = Alignment.Bottom
            ) {
                PosterArt(
                    id = item.id,
                    type = item.type,
                    modifier = Modifier.size(width = 132.dp, height = 196.dp),
                    shape = RoundedCornerShape(18.dp),
                    iconSize = 40.dp
                )
                Spacer(Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.titleFa,
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = item.titleEn,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.7f),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = RoundedCornerShape(50),
                            color = Color.Black.copy(alpha = 0.35f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = RatingGold,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(Modifier.width(5.dp))
                                Text(
                                    text = item.imdbRating.faRating(),
                                    color = Color.White,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(Modifier.width(4.dp))
                                Text(
                                    text = "/۱۰",
                                    color = Color.White.copy(alpha = 0.7f),
                                    style = MaterialTheme.typography.labelSmall
                                )
                            }
                        }
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "امتیاز IMDb",
                            color = Color.White.copy(alpha = 0.85f),
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = yearLabel(item),
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                    if (item.runtimeMinutes != null) {
                        Text(
                            text = item.runtimeMinutes.faRuntime(),
                            color = Color.White.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text(
                        text = "${item.voteCount.faVoteCount()} رای",
                        color = Color.White.copy(alpha = 0.75f),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = item.genres.joinToString(" • ") { it.nameFa },
                        color = Color.White.copy(alpha = 0.9f),
                        style = MaterialTheme.typography.labelMedium,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Row(modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)) {
                HeroFavoriteButton(
                    isFavorite = isFavorite,
                    onToggle = onToggleFavorite
                )
            }
        }
    }
}

@Composable
private fun HeroFavoriteButton(isFavorite: Boolean, onToggle: () -> Unit) {
    Surface(
        onClick = onToggle,
        shape = RoundedCornerShape(50),
        color = Color.White.copy(alpha = 0.12f),
        contentColor = if (isFavorite) FavoriteRed else Color.White,
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = null,
                modifier = Modifier.size(17.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (isFavorite) "در علاقه‌مندی‌ها" else "افزودن به علاقه‌مندی‌ها",
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}

@Composable
private fun CrewSection(item: CatalogItem) {
    item.director?.let { director ->
        SectionTitle("کارگردان")
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCircle(name = director.nameFa, size = 52.dp)
            Spacer(Modifier.width(14.dp))
            Column {
                Text(
                    text = director.nameFa,
                    style = MaterialTheme.typography.titleMedium
                )
                if (director.nameEn.isNotBlank()) {
                    Text(
                        text = director.nameEn,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }

    if (item.cast.isNotEmpty()) {
        SectionTitle("بازیگران")
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(item.cast, key = { it.nameEn.ifBlank { it.nameFa } }) { person ->
                PersonCard(person = person, modifier = Modifier.width(92.dp))
            }
        }
    }
}

@Composable
private fun SeasonCard(season: Season, modifier: Modifier = Modifier) {
    val expanded = remember(season.number) { mutableStateListOf<Int>() }
    val isExpanded = season.number in expanded

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        if (isExpanded) expanded.remove(season.number)
                        else expanded.add(season.number)
                    }
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "فصل ${season.number.fa()}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${season.episodes.size.fa()} قسمت",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Icon(
                    imageVector = Icons.Filled.ExpandMore,
                    contentDescription = if (isExpanded) "بستن فصل" else "باز کردن فصل",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier
                        .size(20.dp)
                        .graphicsLayer { rotationZ = if (isExpanded) 180f else 0f }
                )
            }
            if (isExpanded) {
                season.episodes.forEachIndexed { index, episode ->
                    EpisodeRow(episode = episode)
                    if (index != season.episodes.lastIndex) {
                        androidx.compose.material3.HorizontalDivider(
                            color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
                Spacer(Modifier.height(6.dp))
            }
        }
    }
}

@Composable
private fun EpisodeRow(episode: Episode) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 9.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "قسمت ${episode.number.fa()}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            if (episode.titleEn.isNotBlank()) {
                Text(
                    text = episode.titleEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.primaryContainer
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = null,
                    tint = RatingGold,
                    modifier = Modifier.size(12.dp)
                )
                Spacer(Modifier.width(3.dp))
                Text(
                    text = episode.rating.faRating(),
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 22.dp, bottom = 10.dp)
    )
}

private fun yearLabel(item: CatalogItem): String {
    val year = item.year.toString().toFaDigits()
    return item.yearEnd?.let { "$year – ${it.toString().toFaDigits()}" } ?: year
}
