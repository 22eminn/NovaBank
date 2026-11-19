package com.example.novabank.feature.auth.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.novabank.ui.theme.NovaBankTheme
import com.example.novabank.ui.theme.NovaBlue
import com.example.novabank.ui.theme.NovaNavy

private val MutedText = Color(0xFFC3CAE8)
private val ErrorText = Color(0xFFFF9B9B)
private val EmptyDot = Color(0xFF8C97D1)

// 1) "Route": ViewModel ilə əlaqə burada
@Composable
fun LoginRoute(
    onLoginSuccess: () -> Unit,
    viewModel: LoginViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.isAuthenticated) {
        if (state.isAuthenticated) onLoginSuccess()
    }

    LoginScreen(
        state = state,
        onDigit = viewModel::onDigit,
        onDelete = viewModel::onDelete,
    )
}

// 2) "Screen": yalnız state-ə baxıb çəkir
@Composable
fun LoginScreen(
    state: LoginUiState,
    onDigit: (Int) -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(NovaNavy)
            .systemBarsPadding()
            .padding(horizontal = 32.dp, vertical = 40.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(Modifier.height(56.dp))
        Box(
            modifier = Modifier.size(56.dp).background(NovaBlue, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center,
        ) {
            Text("N", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.height(16.dp))
        Text("NovaBank", style = MaterialTheme.typography.headlineSmall, color = Color.White)
        Spacer(Modifier.height(8.dp))
        Text(
            text = if (state.isError) "PIN yanlışdır, yenidən cəhd et" else "PIN kodunu daxil et",
            style = MaterialTheme.typography.bodyMedium,
            color = if (state.isError) ErrorText else MutedText,
        )
        Spacer(Modifier.height(40.dp))
        PinDots(filledCount = state.pin.length)
        Spacer(Modifier.height(48.dp))
        PinPad(onDigit = onDigit, onDelete = onDelete)
    }
}

@Composable
private fun PinDots(filledCount: Int) {
    Row(
        modifier = Modifier.semantics {
            contentDescription = "PIN: $filledCount / $PIN_LENGTH rəqəm daxil edilib"
        },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        repeat(PIN_LENGTH) { index ->
            val filled = index < filledCount
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .clip(CircleShape)
                    .then(
                        if (filled) Modifier.background(Color.White)
                        else Modifier.border(2.dp, EmptyDot, CircleShape)
                    ),
            )
        }
    }
}

@Composable
private fun PinPad(onDigit: (Int) -> Unit, onDelete: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9)).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEach { digit ->
                    PinKey(onClick = { onDigit(digit) }) { KeyLabel(digit.toString()) }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Spacer(Modifier.size(72.dp)) // biometrik düymənin yeri (sonra əlavə edəcəyik)
            PinKey(onClick = { onDigit(0) }) { KeyLabel("0") }
            PinKey(onClick = onDelete, description = "Sil") { KeyLabel("⌫") }
        }
    }
}

@Composable
private fun PinKey(
    onClick: () -> Unit,
    description: String? = null,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .size(72.dp)
            .clip(CircleShape)
            .background(Color.White.copy(alpha = 0.1f))
            .clickable(role = Role.Button, onClick = onClick)
            .semantics { if (description != null) contentDescription = description },
        contentAlignment = Alignment.Center,
    ) { content() }
}

@Composable
private fun KeyLabel(text: String) {
    Text(text, color = Color.White, fontSize = 26.sp, fontWeight = FontWeight.Medium)
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    NovaBankTheme {
        LoginScreen(state = LoginUiState(pin = "12"), onDigit = {}, onDelete = {})
    }
}
