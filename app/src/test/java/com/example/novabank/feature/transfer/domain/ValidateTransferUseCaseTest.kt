package com.example.novabank.feature.transfer.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ValidateTransferUseCaseTest {

    private val validate = ValidateTransferUseCase()
    private val validCard = "4111111111111111" // açıq test nömrəsi, Luhn-dan keçir

    @Test
    fun validInput_hasNoErrors() {
        assertTrue(validate(validCard, 10_000, 425_080).isValid)
    }

    @Test
    fun cardFailingLuhn_isRejected() {
        assertTrue(validate("4111111111111112", 10_000, 425_080).cardError)
    }

    @Test
    fun shortCardNumber_isRejected() {
        assertTrue(validate("4111", 10_000, 425_080).cardError)
    }

    @Test
    fun amountAboveBalance_isInsufficientFunds() {
        val result = validate(validCard, 500_000, 425_080)
        assertEquals(AmountError.INSUFFICIENT_FUNDS, result.amountError)
    }

    @Test
    fun amountEqualToBalance_isAllowed() {
        assertTrue(validate(validCard, 425_080, 425_080).isValid)
    }

    @Test
    fun zeroOrMissingAmount_isInvalid() {
        assertEquals(AmountError.INVALID, validate(validCard, 0, 425_080).amountError)
        assertEquals(AmountError.INVALID, validate(validCard, null, 425_080).amountError)
    }
}