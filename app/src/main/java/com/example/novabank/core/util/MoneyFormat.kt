package com.example.novabank.core.util

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import kotlin.math.abs

fun formatMoney(minorUnits: Long, showSign: Boolean = false): String {
    val symbols = DecimalFormatSymbols(Locale.ROOT).apply {
        groupingSeparator = '\u00A0'
        decimalSeparator = ','
    }
    val number = DecimalFormat("#,##0.00", symbols)
        .format(BigDecimal.valueOf(abs(minorUnits), 2))
    val sign = when {
        minorUnits < 0 -> "−"
        showSign && minorUnits > 0 -> "+"
        else -> ""
    }
    return "$sign$number\u00A0₼"
}

