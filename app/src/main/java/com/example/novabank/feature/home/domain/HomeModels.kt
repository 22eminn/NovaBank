package com.example.novabank.feature.home.domain

data class Account(
    val ownerName: String,
    val cardLast4: String,
    val balanceMinor: Long,
    val currency: String,
)

data class Transaction(
    val id: String,
    val title: String,
    val dateLabel: String,
    val amountMinor: Long, // mənfi = məxaric, müsbət = mədaxil
)