package com.example.critichub.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.critichub.model.ContentType
import com.example.critichub.navigation.Routes
import com.example.critichub.ui.screens.favorites.FavoritesScreen
import com.example.critichub.ui.screens.home.HomeScreen
import com.example.critichub.ui.screens.profile.ProfileScreen
import com.example.critichub.ui.screens.search.SearchScreen

private data class MainTab(
    val route: String,
    val label: String,
    val icon: ImageVector
)

private val tabs = listOf(
    MainTab(Routes.HOME, "خانه", Icons.Filled.Home),
    MainTab(Routes.SEARCH, "جستجو", Icons.Filled.Search),
    MainTab(Routes.FAVORITES, "علاقه‌مندی‌ها", Icons.Filled.Favorite),
    MainTab(Routes.PROFILE, "پروفایل", Icons.Filled.Person)
)

/** اسکلت اصلی با نوار پایین (خانه/جستجو/علاقه‌مندی‌ها/پروفایل). */
@Composable
fun MainScreen(
    onOpenContent: (String) -> Unit,
    onOpenBrowse: (genreId: String?, type: ContentType?) -> Unit,
    onOpenSettings: () -> Unit,
    onOpenEditProfile: () -> Unit,
    onChangePassword: () -> Unit,
    onLogout: () -> Unit
) {
    val navController = rememberNavController()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp, 0.dp, 0.dp, 0.dp),
        bottomBar = { MainBottomBar(navController) }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            NavHost(navController, startDestination = Routes.HOME) {
                composable(Routes.HOME) {
                    HomeScreen(
                        onOpenContent = onOpenContent,
                        onOpenBrowse = onOpenBrowse,
                        onOpenSearch = {
                            navController.navigate(Routes.SEARCH) {
                                launchSingleTop = true
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                restoreState = true
                            }
                        }
                    )
                }
                composable(Routes.SEARCH) {
                    SearchScreen(onOpenContent = onOpenContent)
                }
                composable(Routes.FAVORITES) {
                    FavoritesScreen(onOpenContent = onOpenContent)
                }
                composable(Routes.PROFILE) {
                    ProfileScreen(
                        onOpenEditProfile = onOpenEditProfile,
                        onOpenSettings = onOpenSettings,
                        onChangePassword = onChangePassword,
                        onOpenContent = onOpenContent,
                        onLogout = onLogout
                    )
                }
            }
        }
    }
}

@Composable
private fun MainBottomBar(navController: NavHostController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar {
        tabs.forEach { tab ->
            val selected = currentRoute == tab.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(tab.route) {
                            launchSingleTop = true
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = tab.icon,
                        contentDescription = tab.label
                    )
                },
                label = { Text(tab.label) }
            )
        }
    }
}
