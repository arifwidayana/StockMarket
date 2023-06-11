package com.arifwidayana.stockmarket.di

import com.arifwidayana.stockmarket.common.csv.CSVParser
import com.arifwidayana.stockmarket.common.csv.CompanyListingsParser
import com.arifwidayana.stockmarket.common.csv.IntraDayInfoParser
import com.arifwidayana.stockmarket.data.network.model.CompanyListing
import com.arifwidayana.stockmarket.data.network.model.IntraDayInfo
import com.arifwidayana.stockmarket.data.repository.StockRepository
import com.arifwidayana.stockmarket.data.repository.StockRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindCompanyListingsParser(companyListingsParser: CompanyListingsParser): CSVParser<CompanyListing>

    @Binds
    @Singleton
    abstract fun bindIntraDayParser(intraDayInfoParser: IntraDayInfoParser): CSVParser<IntraDayInfo>

    @Binds
    @Singleton
    abstract fun bindCStockRepository(stockRepositoryImpl: StockRepositoryImpl): StockRepository
}