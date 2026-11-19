package com.example.novabank.core.database

import com.example.novabank.feature.home.domain.HomeRepository
import com.example.novabank.feature.transfer.domain.TransferRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindHomeRepository(impl: RoomBankRepository): HomeRepository

    @Binds
    abstract fun bindTransferRepository(impl: RoomBankRepository): TransferRepository
}