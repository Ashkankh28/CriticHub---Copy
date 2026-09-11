package com.example.critichub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.critichub.model.CatalogItem
import com.example.critichub.model.ContentType
import com.example.critichub.util.fa
import com.example.critichub.util.faRating
import com.example.critichub.util.toFaDigits

/** ستاره + امتیاز با ارقام فارسی. */
@Composable
fun StarRating(
    rating: Double,
    modifier: Modifier = Modifier,
    starSize: Int = 14,
    textColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = null,
            tint = androidx.compose.ui.graphics.Color(0xFFF5B301),
            modifier = Modifier.size(starSize.dp)
        )
        Spacer(Modifier.width(3.dp))
        Text(
            text = rating.faRating(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = textColor
        )
    }
}

/** عنوان بخش‌ها با امکان «مشاهده همه». */
@Composable
fun SectionHeader(
    title: String,
    modifier: Modifier = Modifier,
    onSeeAll: (() -> Unit)? = null
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.weight(1f)
        )
        if (onSeeAll != null) {
            TextButton(onClick = onSeeAll) {
                Text(text = "مشاهده همه")
            }
        }
    }
}

/** کارت عمودی پوستر برای ردیف‌های افقی صفحهٔ اصلی. */
@Composable
fun PosterCard(
    item: CatalogItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null
) {
    Column(
        modifier = modifier
            .width(148.dp)
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(bottom = 6.dp)
    ) {
        Box(modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.68f)
        ) {
            PosterArt(
                id = item.id,
                type = item.type,
                modifier = Modifier.fillMaxSize(),
                iconSize = 42.dp
            )
            if (onToggleFavorite != null) {
                Box(modifier = Modifier.align(Alignment.TopStart).padding(6.dp)) {
                    FavoriteCircleButton(
                        isFavorite = isFavorite,
                        onToggle = onToggleFavorite,
                        containerColor = Color.Black.copy(alpha = 0.45f)
                    )
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        Column(Modifier.padding(horizontal = 4.dp)) {
            Text(
                text = item.titleFa,
                style = MaterialTheme.typography.titleSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.titleEn,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                StarRating(rating = item.imdbRating, starSize = 12)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.year.fa(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (item.type == ContentType.SERIES) {
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = "سریال",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}

/** کارت افقی (ردیفی) برای فهرست‌ها؛ در جستجو و مرور استفاده می‌شود. */
@Composable
fun ContentRowCard(
    item: CatalogItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onToggleFavorite: (() -> Unit)? = null
) {
    val shape = RoundedCornerShape(18.dp)
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
            .clickable(onClick = onClick)
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PosterArt(
            id = item.id,
            type = item.type,
            modifier = Modifier.size(width = 66.dp, height = 96.dp),
            iconSize = 26.dp
        )
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.titleFa,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.titleEn,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                StarRating(rating = item.imdbRating, starSize = 13)
                Spacer(Modifier.width(8.dp))
                Text(
                    text = item.year.fa(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = if (item.type == ContentType.MOVIE) "فیلم" else "سریال",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            if (item.genres.isNotEmpty()) {
                Spacer(Modifier.height(4.dp))
                Text(
                    text = item.genres.take(2).joinToString(" • ") { it.nameFa }.toFaDigits(),
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        if (onToggleFavorite != null) {
            Spacer(Modifier.width(6.dp))
            FavoriteCircleButton(
                isFavorite = isFavorite,
                onToggle = onToggleFavorite,
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        }
    }
}

/** دکمهٔ دایره‌ای علاقه‌مندی برای استفاده روی کارت‌ها. */
@Composable
fun FavoriteCircleButton(
    isFavorite: Boolean,
    onToggle: () -> Unit,
    containerColor: Color,
    modifier: Modifier = Modifier
) {
    androidx.compose.material3.IconButton(
        onClick = onToggle,
        modifier = modifier
            .size(32.dp)
            .clip(CircleShape)
            .background(containerColor)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
            contentDescription = if (isFavorite) "حذف از علاقه‌مندی‌ها" else "افزودن به علاقه‌مندی‌ها",
            tint = if (isFavorite) Color(0xFFE5484D) else Color.White,
            modifier = Modifier.size(17.dp)
        )
    }
}
