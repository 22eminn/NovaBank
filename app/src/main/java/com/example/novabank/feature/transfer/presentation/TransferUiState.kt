package com.example.novabank.feature.transfer.presentation

import com.example.novabank.feature.transfer.domain.AmountError
import com.example.novabank.feature.transfer.domain.TransferReceipt

data class TransferUiState(
    val balanceMinor: Long = 0,
    val cardInput: String = "",   // yalnız rəqəmlər
    val amountInput: String = "", // "150,50" kimi
    val cardError: Boolean = false,
    val amountError: AmountError? = null,
    val isSubmitting: Boolean = false,
    val receipt: TransferReceipt? = null,
    val errorMessage: String? = null,
)