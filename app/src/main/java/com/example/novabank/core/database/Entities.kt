package com.example.novabank.core.database

import androidx.room3.Entity
import androidx.room3.PrimaryKey

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey val id: Long,
    val ownerName: String,
    val cardLast4: String,
    val balanceMinor: Long,
    val currency: String,
)

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val createdAt: Long, // millisaniyə, tarix etiketini sonra formatlayacağıq
    val amountMinor: Long,
)