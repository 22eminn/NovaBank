package com.example.novabank.core.navigation


import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.novabank.core.designsystem.PlaceholderScreen
import com.example.novabank.feature.auth.presentation.LoginRoute

@Composable
fun NovaNavHost(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

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
        composable(Routes.HOME) {
            PlaceholderScreen(title = "Ana səhifə")
        }
    }
}