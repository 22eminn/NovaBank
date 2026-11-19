package com.example.novabank.feature.auth.presentation

const val PIN_LENGTH = 4

data class LoginUiState(
    val pin: String = "",
    val isError: Boolean = false,
    val isAuthenticated: Boolean = false,
)