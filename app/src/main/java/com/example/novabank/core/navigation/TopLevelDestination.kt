package com.example.novabank.core.navigation

import androidx.annotation.DrawableRes
import com.example.novabank.R

data class TopLevelDestination(
    val route: String,
    val label: String,
    @DrawableRes val icon: Int,
)

val topLevelDestinations = listOf(
    TopLevelDestination(Routes.HOME, "Ana səhifə", R.drawable.ic_home),
    TopLevelDestination(Routes.CARDS, "Kartlar", R.drawable.ic_credit_card),
    TopLevelDestination(Routes.PAYMENTS, "Ödənişlər", R.drawable.ic_payments),
    TopLevelDestination(Routes.PROFILE, "Profil", R.drawable.ic_person),
)