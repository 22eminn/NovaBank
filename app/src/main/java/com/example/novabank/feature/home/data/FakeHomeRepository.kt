package com.example.novabank.feature.home.data

import com.example.novabank.feature.home.domain.Account
import com.example.novabank.feature.home.domain.HomeRepository
import com.example.novabank.feature.home.domain.Transaction
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay

@Singleton
class FakeHomeRepository @Inject constructor() : HomeRepository {

    override suspend fun getAccount(): Account {
        delay(600) // şəbəkə gecikməsini simulyasiya edir
        return Account(
            ownerName = "Aysel",
            cardLast4 = "4821",
            balanceMinor = 425_080,
            currency = "AZN",
        )
    }

    override suspend fun getRecentTransactions(): List<Transaction> {
        delay(300)
        return listOf(
            Transaction("1", "Supermarket", "Bu gün, 14:20", -3_240),
            Transaction("2", "Əməkhaqqı", "Dünən", 180_000),
            Transaction("3", "Metro kartı", "Dünən", -500),
            Transaction("4", "Mobil ödəniş", "8 Noy", -1_000),
        )
    }
}