package com.example.novabank.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.novabank.R

val Inter = FontFamily.Default

private val base = Typography()

val NovaTypography = Typography(
    displaySmall = base.displaySmall.copy(
        fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 34.sp, lineHeight = 40.sp
    ), // balans: "4 250,80 ₼"
    headlineSmall = base.headlineSmall.copy(
        fontFamily = Inter, fontWeight = FontWeight.Bold, fontSize = 24.sp
    ), // ekran başlıqları
    titleMedium = base.titleMedium.copy(
        fontFamily = Inter, fontWeight = FontWeight.SemiBold, fontSize = 16.sp
    ),
    bodyLarge = base.bodyLarge.copy(fontFamily = Inter, fontSize = 16.sp),
    bodyMedium = base.bodyMedium.copy(fontFamily = Inter, fontSize = 15.sp),
    labelMedium = base.labelMedium.copy(
        fontFamily = Inter, fontWeight = FontWeight.Medium, fontSize = 12.sp
    ),
)