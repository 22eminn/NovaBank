package com.example.novabank.feature.transfer.domain

import javax.inject.Inject

class ValidateTransferUseCase @Inject constructor() {

    operator fun invoke(
        cardDigits: String,
        amountMinor: Long?,
        balanceMinor: Long,
    ): TransferValidation {
        val amountError = when {
            amountMinor == null || amountMinor <= 0 -> AmountError.INVALID
            amountMinor > balanceMinor -> AmountError.INSUFFICIENT_FUNDS
            else -> null
        }
        return TransferValidation(
            cardError = !isValidCardNumber(cardDigits),
            amountError = amountError,
        )
    }
}

/** Luhn alqoritmi: sağdan başlayaraq hər ikinci rəqəm 2-yə vurulur. */
fun isValidCardNumber(digits: String): Boolean {
    if (digits.length != 16 || !digits.all { it.isDigit() }) return false
    var sum = 0
    digits.reversed().forEachIndexed { index, char ->
        var digit = char - '0'
        if (index % 2 == 1) {
            digit *= 2
            if (digit > 9) digit -= 9
        }
        sum += digit
    }
    return sum % 10 == 0
}