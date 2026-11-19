package com.example.novabank.core.database

import androidx.room3.Dao
import androidx.room3.Insert
import androidx.room3.Query
import androidx.room3.Transaction
import kotlinx.coroutines.flow.Flow

@Dao
abstract class BankDao {

    @Query("SELECT * FROM accounts LIMIT 1")
    abstract fun observeAccount(): Flow<AccountEntity?>

    @Query("SELECT * FROM transactions ORDER BY createdAt DESC LIMIT :limit")
    abstract fun observeRecentTransactions(limit: Int): Flow<List<TransactionEntity>>

    @Query("SELECT COUNT(*) FROM accounts")
    abstract suspend fun accountCount(): Int

    @Insert
    abstract suspend fun insertAccount(account: AccountEntity)

    @Insert
    abstract suspend fun insertTransaction(transaction: TransactionEntity)

    @Insert
    abstract suspend fun insertTransactions(transactions: List<TransactionEntity>)

    /** Balans kifayətdirsə azaldır və 1 qaytarır, əks halda heç nə dəyişmir və 0 qaytarır. */
    @Query(
        "UPDATE accounts SET balanceMinor = balanceMinor - :amount " +
                "WHERE id = :id AND balanceMinor >= :amount"
    )
    abstract suspend fun debitIfEnough(id: Long, amount: Long): Int

    @Transaction
    open suspend fun applyTransfer(
        accountId: Long,
        amountMinor: Long,
        transaction: TransactionEntity,
    ): Boolean {
        if (debitIfEnough(accountId, amountMinor) == 0) return false
        insertTransaction(transaction)
        return true
    }

    @Transaction
    open suspend fun seed(account: AccountEntity, transactions: List<TransactionEntity>) {
        insertAccount(account)
        insertTransactions(transactions)
    }
}