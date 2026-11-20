package com.example.novabank.feature.transfer.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.novabank.R
import com.example.novabank.core.util.formatMoney
import com.example.novabank.feature.transfer.domain.AmountError
import com.example.novabank.feature.transfer.domain.TransferReceipt
import com.example.novabank.ui.theme.NovaBankTheme

@Composable
fun TransferRoute(
    onBack: () -> Unit,
    viewModel: TransferViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    TransferScreen(
        state = state,
        onCardChange = viewModel::onCardChange,
        onAmountChange = viewModel::onAmountChange,
        onSubmit = viewModel::onSubmit,
        onBack = onBack,
    )
}

@Composable
fun TransferScreen(
    state: TransferUiState,
    onCardChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val receipt = state.receipt
    if (receipt != null) {
        TransferSuccess(receipt = receipt, onDone = onBack)
    } else {
        TransferForm(state, onCardChange, onAmountChange, onSubmit, onBack)
    }
}

@Composable
private fun TransferForm(
    state: TransferUiState,
    onCardChange: (String) -> Unit,
    onAmountChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onBack: () -> Unit,
) {
    val amountMessage = when (state.amountError) {
        AmountError.INVALID -> "Məbləği düzgün daxil et"
        AmountError.INSUFFICIENT_FUNDS -> "Balans kifayət etmir"
        null -> null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .imePadding(),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = onBack) {
                Icon(painterResource(R.drawable.ic_arrow_back), contentDescription = "Geri")
            }
            Text("Köçürmə", style = MaterialTheme.typography.headlineSmall)
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
        ) {
            Text(
                "Haradan",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(8.dp))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
            ) {
                Column(Modifier.padding(16.dp)) {
                    Text("Debet kart", style = MaterialTheme.typography.titleMedium)
                    Text(
                        "Mövcud: ${formatMoney(state.balanceMinor)}",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            Spacer(Modifier.height(20.dp))
            OutlinedTextField(
                value = state.cardInput,
                onValueChange = onCardChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Alıcı kartı") },
                singleLine = true,
                isError = state.cardError,
                visualTransformation = CardNumberTransformation,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                supportingText = {
                    if (state.cardError) Text("Kart nömrəsi yanlışdır")
                },
            )

            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = state.amountInput,
                onValueChange = onAmountChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Məbləğ") },
                suffix = { Text("₼") },
                singleLine = true,
                isError = state.amountError != null,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal,
                    imeAction = ImeAction.Done,
                ),
                supportingText = {
                    if (amountMessage != null) Text(amountMessage)
                },
            )

            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                listOf("50", "100", "200").forEach { amount ->
                    AssistChip(
                        onClick = { onAmountChange(amount) },
                        label = { Text("$amount ₼") },
                    )
                }
            }
        }

        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            state.errorMessage?.let {
                Text(
                    it,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Button(
                onClick = onSubmit,
                enabled = !state.isSubmitting,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                if (state.isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                    )
                } else {
                    Text("Davam et")
                }
            }
        }
    }
}

@Composable
private fun TransferSuccess(receipt: TransferReceipt, onDone: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Surface(
            modifier = Modifier.size(88.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(R.drawable.ic_check_circle),
                    contentDescription = null,
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text("Köçürmə uğurludur", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        Text(formatMoney(receipt.amountMinor), style = MaterialTheme.typography.displaySmall)
        Spacer(Modifier.height(16.dp))
        Text(
            "Alıcı: ${receipt.recipientMasked}",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Text(
            "Əməliyyat nömrəsi: ${receipt.reference}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(Modifier.height(40.dp))
        Button(
            onClick = onDone,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(16.dp),
        ) { Text("Ana səhifəyə qayıt") }
    }
}

/** Saxlanılan dəyər: yalnız rəqəmlər. Göstərilən: 4-4-4-4 qruplarla. */
private object CardNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = text.text.chunked(4).joinToString(" ")
        val offsets = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                offset + (offset - 1).coerceAtLeast(0) / 4

            override fun transformedToOriginal(offset: Int): Int = offset - offset / 5
        }
        return TransformedText(androidx.compose.ui.text.AnnotatedString(formatted), offsets)
    }
}

@Preview(showBackground = true)
@Composable
private fun TransferScreenPreview() {
    NovaBankTheme {
        TransferScreen(
            state = TransferUiState(
                balanceMinor = 425_080,
                cardInput = "4111111111",
                amountInput = "50"
            ),
            onCardChange = {},
            onAmountChange = {},
            onSubmit = {},
            onBack = {},
        )
    }
}