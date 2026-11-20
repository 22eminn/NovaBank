package com.example.novabank.feature.home.presentation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.novabank.R
import com.example.novabank.core.util.formatMoney
import com.example.novabank.feature.home.domain.Account
import com.example.novabank.feature.home.domain.Transaction
import com.example.novabank.ui.theme.NovaBankTheme
import com.example.novabank.ui.theme.PositiveGreen

@Composable
fun HomeRoute(
    onTransferClick: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    HomeScreen(state = state, onRetry = viewModel::load, onTransferClick = onTransferClick)
}

@Composable
fun HomeScreen(
    state: HomeUiState,
    onRetry: () -> Unit,
    onTransferClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize().statusBarsPadding()) {
        when (state) {
            HomeUiState.Loading ->
                CircularProgressIndicator(Modifier.align(Alignment.Center))

            is HomeUiState.Error ->
                ErrorContent(state.message, onRetry, Modifier.align(Alignment.Center))

            is HomeUiState.Success -> HomeContent(state, onTransferClick)
        }
    }
}

@Composable
private fun HomeContent(state: HomeUiState.Success, onTransferClick: () -> Unit) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        item { Header(name = state.account.ownerName) }
        item { BalanceCard(state.account) }
        item { QuickActions(onTransferClick) }
        item {
            Text("Son əməliyyatlar", style = MaterialTheme.typography.titleMedium)
        }
        item { TransactionList(state.transactions) }
    }
}

@Composable
private fun Header(name: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(
                "Xoş gəldin",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(name, style = MaterialTheme.typography.headlineSmall)
        }
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                name.take(1),
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun BalanceCard(account: Account) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        color = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                "Ümumi balans",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
            )
            Spacer(Modifier.height(6.dp))
            Text(
                formatMoney(account.balanceMinor),
                style = MaterialTheme.typography.displaySmall,
            )
            Spacer(Modifier.height(18.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Debet •••• ${account.cardLast4}", style = MaterialTheme.typography.bodyMedium)
                Text(account.currency, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun QuickActions(onTransferClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        QuickAction(R.drawable.ic_send, "Köçürmə", onClick = onTransferClick)
        QuickAction(R.drawable.ic_payments, "Ödəniş")
        QuickAction(R.drawable.ic_currency_exchange, "Məzənnə")
        QuickAction(R.drawable.ic_more_horiz, "Daha çox")
    }
}

@Composable
private fun QuickAction(
    @DrawableRes icon: Int,
    label: String,
    onClick: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(role = Role.Button, onClick = onClick)
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Surface(
            modifier = Modifier.size(52.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                )
            }
        }
        Text(label, style = MaterialTheme.typography.labelMedium)
    }
}

@Composable
private fun TransactionList(transactions: List<Transaction>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        color = MaterialTheme.colorScheme.surface,
    ) {
        Column(Modifier.padding(horizontal = 16.dp)) {
            transactions.forEachIndexed { index, transaction ->
                TransactionRow(transaction)
                if (index < transactions.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.outline)
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                transaction.title.take(1),
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(Modifier.weight(1f)) {
            Text(transaction.title, style = MaterialTheme.typography.bodyMedium)
            Text(
                transaction.dateLabel,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            formatMoney(transaction.amountMinor, showSign = true),
            style = MaterialTheme.typography.titleMedium,
            color = if (transaction.amountMinor > 0) PositiveGreen
            else MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(message, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRetry) { Text("Yenidən cəhd et") }
    }
}

@Preview(showBackground = true)
@Composable
private fun HomeScreenPreview() {
    NovaBankTheme {
        HomeScreen(
            state = HomeUiState.Success(
                account = Account("Aysel", "4821", 425_080, "AZN"),
                transactions = listOf(
                    Transaction("1", "Supermarket", "Bu gün, 14:20", -3_240),
                    Transaction("2", "Əməkhaqqı", "Dünən", 180_000),
                ),
            ),
            onRetry = {},
            onTransferClick = {},
        )
    }
}