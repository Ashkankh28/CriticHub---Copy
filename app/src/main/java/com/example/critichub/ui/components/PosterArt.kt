package com.example.critichub.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.critichub.model.ContentType

/** پالت‌های گرادیانی سینمایی برای پوسترها. */
val PosterPalettes: List<Pair<Color, Color>> = listOf(
    Color(0xFF1D4ED8) to Color(0xFF0A1022),
    Color(0xFF0E7490) to Color(0xFF061A26),
    Color(0xFF3730A3) to Color(0xFF0D0A24),
    Color(0xFF1E3A8A) to Color(0xFF0B1020),
    Color(0xFF075985) to Color(0xFF071726),
    Color(0xFF0F5F5A) to Color(0xFF041B18),
    Color(0xFF4338CA) to Color(0xFF141031),
    Color(0xFF274B7D) to Color(0xFF060B16)
)

/** شکل پیش‌فرض پوسترها. */
val PosterShape = RoundedCornerShape(14.dp)

/** انتخاب ثابت پالت بر اساس شناسهٔ آیتم. */
fun posterColors(id: String): Pair<Color, Color> {
    val index = (id.hashCode() and Int.MAX_VALUE) % PosterPalettes.size
    return PosterPalettes[index]
}

/**
 * پوستر گرافیکی (گرادیان + آیکون نوع محتوا).
 * زمانی که API واقعی متصل شود و [com.example.critichub.model.CatalogItem.posterUrl]
 * در دسترس باشد، این بخش با بارگذاری تصویر جایگزین می‌شود.
 */
@Composable
fun PosterArt(
    id: String,
    type: ContentType,
    modifier: Modifier = Modifier,
    shape: Shape = PosterShape,
    iconSize: Dp = 44.dp
) {
    val (top, bottom) = remember(id) { posterColors(id) }
    val icon: ImageVector = if (type == ContentType.MOVIE) Icons.Filled.Movie else Icons.Filled.Tv
    Box(
        modifier = modifier
            .clip(shape)
            .background(Brush.verticalGradient(listOf(top, bottom))),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.5f),
            modifier = Modifier.size(iconSize)
        )
    }
}

/** رنگ‌های آواتار بر اساس نام. */
private val AvatarColors = listOf(
    Color(0xFF1D4ED8), Color(0xFF0E7490), Color(0xFF7C3AED),
    Color(0xFFBE185D), Color(0xFFB45309), Color(0xFF047857)
)

fun avatarColor(seed: String): Color {
    val index = (seed.hashCode() and Int.MAX_VALUE) % AvatarColors.size
    return AvatarColors[index]
}
