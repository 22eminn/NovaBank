package com.example.novabank.core.database

import android.content.Context
import androidx.room3.Room
import androidx.sqlite.driver.AndroidSQLiteDriver
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): NovaDatabase =
        Room.databaseBuilder<NovaDatabase>(context, "nova.db")
            .setDriver(AndroidSQLiteDriver())
            .build()

    @Provides
    fun provideBankDao(database: NovaDatabase): BankDao = database.bankDao()
}