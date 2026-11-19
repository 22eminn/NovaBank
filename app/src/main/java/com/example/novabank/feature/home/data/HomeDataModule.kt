package com.example.novabank.feature.home.data

import com.example.novabank.feature.home.domain.HomeRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class HomeDataModule {
    @Binds
    abstract fun bindHomeRepository(impl: FakeHomeRepository): HomeRepository
}