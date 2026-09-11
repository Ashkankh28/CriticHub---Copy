package com.example.critichub.ui.screens.profile

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.critichub.model.User
import com.example.critichub.ui.components.AvatarCircle
import com.example.critichub.ui.components.EmptyView
import com.example.critichub.ui.screens.favorites.FavoriteRowCard
import com.example.critichub.ui.theme.FavoriteRed
import com.example.critichub.util.fa
import com.example.critichub.viewmodel.ProfileViewModel

/** صفحهٔ پروفایل کاربر همراه با بخش علاقه‌مندی‌ها. */
@Composable
fun ProfileScreen(
    onOpenEditProfile: () -> Unit,
    onOpenSettings: () -> Unit,
    onChangePassword: () -> Unit,
    onOpenContent: (String) -> Unit,
    onLogout: () -> Unit
) {
    val viewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var favoritesTab by rememberSaveable { mutableIntStateOf(0) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    val user = uiState.user
    val movies = uiState.favorites.filter { it.type == com.example.critichub.model.ContentType.MOVIE }
    val series = uiState.favorites.filter { it.type == com.example.critichub.model.ContentType.SERIES }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("خروج از حساب") },
            text = { Text("از حساب کاربری خود خارج می‌شوید؟") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showLogoutDialog = false
                        viewModel.logout()
                        onLogout()
                    }
                ) {
                    Text("خروج", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("انصراف")
                }
            }
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp)
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .statusBarsPadding(),
            contentPadding = PaddingValues(bottom = 20.dp)
        ) {
            item(key = "title") {
                Text(
                    text = "پروفایل",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp, top = 10.dp, end = 20.dp, bottom = 8.dp)
                )
            }

            item(key = "user") {
                UserCard(user = user)
            }

            item(key = "actions") {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)) {
                    MenuRow(
                        icon = Icons.Filled.Edit,
                        title = "ویرایش پروفایل",
                        onClick = onOpenEditProfile
                    )
                    MenuRow(
                        icon = Icons.Filled.Settings,
                        title = "تنظیمات",
                        onClick = onOpenSettings
                    )
                    MenuRow(
                        icon = Icons.Filled.Password,
                        title = "تغییر رمز عبور",
                        onClick = onChangePassword
                    )
                    MenuRow(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "خروج از حساب",
                        iconTint = MaterialTheme.colorScheme.error,
                        titleColor = MaterialTheme.colorScheme.error,
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            item(key = "fav-title") {
                Text(
                    text = "علاقه‌مندی‌ها",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 10.dp, bottom = 4.dp)
                )
            }
            item(key = "fav-tabs") {
                TabRow(
                    selectedTabIndex = favoritesTab,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Tab(
                        selected = favoritesTab == 0,
                        onClick = { favoritesTab = 0 },
                        text = { Text("فیلم‌ها (${movies.size.fa()})") }
                    )
                    Tab(
                        selected = favoritesTab == 1,
                        onClick = { favoritesTab = 1 },
                        text = { Text("سریال‌ها (${series.size.fa()})") }
                    )
                }
            }

            val currentFavorites = if (favoritesTab == 0) movies else series
            if (currentFavorites.isEmpty()) {
                item(key = "fav-empty") {
                    EmptyView(
                        title = if (favoritesTab == 0)
                            "فیلمی در علاقه‌مندی‌ها نیست"
                        else
                            "سریالی در علاقه‌مندی‌ها نیست",
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            } else {
                items(
                    count = currentFavorites.size,
                    key = { currentFavorites[it].contentId }
                ) { index ->
                    val favorite = currentFavorites[index]
                    FavoriteRowCard(
                        favorite = favorite,
                        onClick = { onOpenContent(favorite.contentId) },
                        onRemove = { viewModel.removeFavorite(favorite.contentId) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun UserCard(user: User?) {
    if (user == null) return
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Row(
            modifier = Modifier.padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AvatarCircle(name = user.fullName, size = 64.dp)
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = user.fullName,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "@${user.username}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Filled.Email,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    Text(
                        text = user.email,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun MenuRow(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    titleColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconTint,
            modifier = Modifier.size(22.dp)
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = titleColor,
            modifier = Modifier.weight(1f)
        )
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
        )
    }
}
