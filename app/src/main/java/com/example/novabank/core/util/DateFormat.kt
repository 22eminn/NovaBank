package com.example.novabank.core.util

import java.time.Instant
import java.time.ZoneId
import java.util.Locale

private val MONTHS = listOf(
    "Yan", "Fev", "Mar", "Apr", "May", "İyn",
    "İyl", "Avq", "Sen", "Okt", "Noy", "Dek",
)

fun formatDateLabel(
    createdAt: Long,
    now: Instant = Instant.now(),
    zone: ZoneId = ZoneId.systemDefault(),
): String {
    val dateTime = Instant.ofEpochMilli(createdAt).atZone(zone)
    val today = now.atZone(zone).toLocalDate()
    return when (dateTime.toLocalDate()) {
        today -> String.format(Locale.ROOT, "Bu gün, %02d:%02d", dateTime.hour, dateTime.minute)
        today.minusDays(1) -> "Dünən"
        else -> "${dateTime.dayOfMonth} ${MONTHS[dateTime.monthValue - 1]}"
    }
}