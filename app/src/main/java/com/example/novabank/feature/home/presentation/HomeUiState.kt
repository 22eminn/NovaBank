package com.example.novabank.feature.home.presentation

import com.example.novabank.feature.home.domain.Account
import com.example.novabank.feature.home.domain.Transaction

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(
        val account: Account,
        val transactions: List<Transaction>,
    ) : HomeUiState
    data class Error(val message: String) : HomeUiState
}