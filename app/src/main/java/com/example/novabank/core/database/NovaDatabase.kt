package com.example.novabank.core.database

import androidx.room3.Database
import androidx.room3.RoomDatabase

@Database(
    entities = [AccountEntity::class, TransactionEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class NovaDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao
}