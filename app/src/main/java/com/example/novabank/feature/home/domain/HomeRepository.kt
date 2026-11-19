package com.example.novabank.feature.home.domain

import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun observeAccount(): Flow<Account>
    fun observeRecentTransactions(): Flow<List<Transaction>>
}