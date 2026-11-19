package com.example.novabank.core.database

import com.example.novabank.core.util.formatDateLabel
import com.example.novabank.feature.home.domain.Account
import com.example.novabank.feature.home.domain.HomeRepository
import com.example.novabank.feature.home.domain.Transaction
import com.example.novabank.feature.transfer.domain.InsufficientFundsException
import com.example.novabank.feature.transfer.domain.TransferReceipt
import com.example.novabank.feature.transfer.domain.TransferRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

private const val ACCOUNT_ID = 1L
private const val RECENT_LIMIT = 5
private const val HOUR = 3_600_000L

@Singleton
class RoomBankRepository @Inject constructor(
    private val dao: BankDao,
) : HomeRepository, TransferRepository {

    private val seedMutex = Mutex()

    override fun observeAccount(): Flow<Account> =
        dao.observeAccount()
            .onStart { ensureSeeded() }
            .filterNotNull()
            .map { it.toDomain() }

    override fun observeRecentTransactions(): Flow<List<Transaction>> =
        dao.observeRecentTransactions(RECENT_LIMIT)
            .map { list -> list.map { it.toDomain() } }

    override suspend fun transfer(cardDigits: String, amountMinor: Long): TransferReceipt {
        delay(1_200) // şəbəkə gecikməsini simulyasiya edir
        val id = UUID.randomUUID().toString()
        val recipient = "•••• ${cardDigits.takeLast(4)}"
        val applied = dao.applyTransfer(
            accountId = ACCOUNT_ID,
            amountMinor = amountMinor,
            transaction = TransactionEntity(
                id = id,
                title = "Köçürmə $recipient",
                createdAt = System.currentTimeMillis(),
                amountMinor = -amountMinor,
            ),
        )
        if (!applied) throw InsufficientFundsException()
        return TransferReceipt(
            reference = id.take(8).uppercase(),
            amountMinor = amountMinor,
            recipientMasked = recipient,
        )
    }

    private suspend fun ensureSeeded() {
        seedMutex.withLock {
            if (dao.accountCount() > 0) return
            val now = System.currentTimeMillis()
            dao.seed(
                account = AccountEntity(ACCOUNT_ID, "Aysel", "4821", 425_080, "AZN"),
                transactions = listOf(
                    TransactionEntity("t1", "Supermarket", now - 1 * HOUR, -3_240),
                    TransactionEntity("t2", "Əməkhaqqı", now - 24 * HOUR, 180_000),
                    TransactionEntity("t3", "Metro kartı", now - 26 * HOUR, -500),
                    TransactionEntity("t4", "Mobil ödəniş", now - 120 * HOUR, -1_000),
                ),
            )
        }
    }
}

private fun AccountEntity.toDomain() =
    Account(ownerName, cardLast4, balanceMinor, currency)

private fun TransactionEntity.toDomain() =
    Transaction(id, title, formatDateLabel(createdAt), amountMinor)