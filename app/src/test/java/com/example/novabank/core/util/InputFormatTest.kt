package com.example.novabank.core.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class InputFormatTest {

    @Test
    fun parse_convertsToMinorUnits() {
        assertEquals(15_050L, parseAmountToMinor("150,50"))
        assertEquals(15_000L, parseAmountToMinor("150"))
        assertEquals(50L, parseAmountToMinor(",5"))
    }

    @Test
    fun parse_returnsNullForBlankOrGarbage() {
        assertNull(parseAmountToMinor(""))
        assertNull(parseAmountToMinor("abc"))
    }

    @Test
    fun sanitize_cleansUserInput() {
        assertEquals("150,5", sanitizeAmountInput("150.5"))
        assertEquals("150,55", sanitizeAmountInput("150,555"))
        assertEquals("12", sanitizeAmountInput("1a2"))
    }

    @Test
    fun formatCard_groupsByFour() {
        assertEquals("4111 1111 11", formatCard("4111111111"))
    }
}