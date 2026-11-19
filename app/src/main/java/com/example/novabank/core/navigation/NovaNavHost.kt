package com.example.novabank.core.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.novabank.core.designsystem.PlaceholderScreen
import com.example.novabank.feature.auth.presentation.LoginRoute
import com.example.novabank.feature.home.presentation.HomeRoute

@Composable
fun NovaNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN,
        modifier = modifier,
    ) {
        composable(Routes.LOGIN) {
            LoginRoute(
                onLoginSuccess = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.HOME) { HomeRoute() }
        composable(Routes.CARDS) { PlaceholderScreen(title = "Kartlar") }
        composable(Routes.PAYMENTS) { PlaceholderScreen(title = "Ödənişlər") }
        composable(Routes.PROFILE) { PlaceholderScreen(title = "Profil") }
    }
}