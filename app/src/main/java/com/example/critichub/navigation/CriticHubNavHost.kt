package com.example.critichub.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.critichub.di.AppContainer
import com.example.critichub.model.ContentType
import com.example.critichub.ui.screens.MainScreen
import com.example.critichub.ui.screens.auth.LoginScreen
import com.example.critichub.ui.screens.auth.RegisterScreen
import com.example.critichub.ui.screens.auth.SplashScreen
import com.example.critichub.ui.screens.browse.BrowseScreen
import com.example.critichub.ui.screens.detail.DetailScreen
import com.example.critichub.ui.screens.profile.EditProfileScreen
import com.example.critichub.ui.screens.settings.ChangePasswordScreen
import com.example.critichub.ui.screens.settings.SettingsScreen

/**
 * نویگیشن ریشهٔ برنامه:
 * Splash → (Login | Main) → Detail/Browse/Settings/…
 */
@Composable
fun CriticHubNavHost(container: AppContainer) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                hasSession = container.preferences.sessionUserId.value != null,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.LOGIN) {
            LoginScreen(
                onGoRegister = { navController.navigate(Routes.REGISTER) },
                onForgotPassword = { navController.navigate(Routes.CHANGE_PASSWORD) },
                onLoggedIn = { navController.navigate(Routes.MAIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Routes.REGISTER) {
            RegisterScreen(
                onGoLogin = { navController.popBackStack() },
                onRegistered = { navController.navigate(Routes.MAIN) { popUpTo(0) { inclusive = true } } }
            )
        }

        composable(Routes.CHANGE_PASSWORD) {
            ChangePasswordScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.MAIN) {
            MainScreen(
                onOpenContent = { contentId ->
                    navController.navigate(Routes.detail(contentId))
                },
                onOpenBrowse = { genreId, type ->
                    navController.navigate(Routes.browse(genreId, type))
                },
                onOpenSettings = { navController.navigate(Routes.SETTINGS) },
                onOpenEditProfile = { navController.navigate(Routes.EDIT_PROFILE) },
                onChangePassword = { navController.navigate(Routes.CHANGE_PASSWORD) },
                onLogout = {
                    navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } }
                }
            )
        }

        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("contentId") { type = NavType.StringType })
        ) {
            DetailScreen(onBack = { navController.popBackStack() })
        }

        composable(
            route = Routes.BROWSE,
            arguments = listOf(
                navArgument("genreId") {
                    type = NavType.StringType
                    defaultValue = "all"
                },
                navArgument("type") {
                    type = NavType.StringType
                    defaultValue = "all"
                }
            )
        ) {
            BrowseScreen(
                onBack = { navController.popBackStack() },
                onOpenContent = { contentId ->
                    navController.navigate(Routes.detail(contentId))
                }
            )
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.EDIT_PROFILE) {
            EditProfileScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() }
            )
        }
    }
}
