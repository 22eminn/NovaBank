package com.example.novabank.feature.home.domain

interface HomeRepository {
    suspend fun getAccount(): Account
    suspend fun getRecentTransactions(): List<Transaction>
}