package com.example.novabank.core.util

/** Kart nömrəsini 4-4-4-4 qruplarla göstərir: "4111 1111 1111 1111" */
fun formatCard(digits: String): String = digits.chunked(4).joinToString(" ")

/** İstifadəçinin yazdığı məbləği təmizləyir: yalnız rəqəm, bir vergül, 2 onluq. */
fun sanitizeAmountInput(raw: String): String {
    val normalized = raw.replace('.', ',')
    val commaIndex = normalized.indexOf(',')
    val integerPart = (if (commaIndex == -1) normalized else normalized.substring(0, commaIndex))
        .filter { it.isDigit() }
        .take(7)
    if (commaIndex == -1) return integerPart
    val decimalPart = normalized.substring(commaIndex + 1).filter { it.isDigit() }.take(2)
    return "$integerPart,$decimalPart"
}

fun parseAmountToMinor(input: String): Long? {
    if (input.isBlank()) return null
    return input.replace(',', '.').toBigDecimalOrNull()?.movePointRight(2)?.toLong()
}
