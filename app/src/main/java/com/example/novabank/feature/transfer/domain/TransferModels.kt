package com.example.novabank.feature.transfer.domain

enum class AmountError { INVALID, INSUFFICIENT_FUNDS }

data class TransferValidation(
    val cardError: Boolean,
    val amountError: AmountError?,
) {
    val isValid: Boolean get() = !cardError && amountError == null
}

data class TransferReceipt(
    val reference: String,
    val amountMinor: Long,
    val recipientMasked: String,
)
class InsufficientFundsException : Exception()