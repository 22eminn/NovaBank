package com.example.novabank.feature.transfer.domain

interface TransferRepository {
    suspend fun transfer(cardDigits: String, amountMinor: Long): TransferReceipt
}