package com.arifwidayana.stockmarket.data.repository

import com.arifwidayana.stockmarket.common.csv.CompanyListingsParser
import com.arifwidayana.stockmarket.common.csv.IntraDayInfoParser
import com.arifwidayana.stockmarket.common.wrapper.Resource
import com.arifwidayana.stockmarket.data.local.StockDatabase
import com.arifwidayana.stockmarket.data.mapper.toCompanyInfo
import com.arifwidayana.stockmarket.data.mapper.toCompanyListing
import com.arifwidayana.stockmarket.data.mapper.toCompanyListingEntity
import com.arifwidayana.stockmarket.data.network.model.CompanyInfo
import com.arifwidayana.stockmarket.data.network.model.CompanyListing
import com.arifwidayana.stockmarket.data.network.model.IntraDayInfo
import com.arifwidayana.stockmarket.data.network.service.StockService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

interface StockRepository {
    suspend fun getCompanyListings(fetchFromRemote: Boolean, query: String): Flow<Resource<List<CompanyListing>>>
    suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>>
    suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo>
}

@Singleton
class StockRepositoryImpl @Inject constructor(
    private val stockService: StockService,
    private val stockDatabase: StockDatabase,
    private val companyListingsParser: CompanyListingsParser,
    private val intraDayInfoParser: IntraDayInfoParser
) : StockRepository {
    override suspend fun getCompanyListings(
        fetchFromRemote: Boolean,
        query: String
    ): Flow<Resource<List<CompanyListing>>> = flow {
        val localListing = stockDatabase.stockDao.searchCompanyListing(query)
        val isDbEmpty = localListing.isEmpty() && query.isBlank()
        emit(Resource.Loading(true))
        emit(Resource.Success(localListing.map {
            it.toCompanyListing()
        }))
        if (!isDbEmpty && !fetchFromRemote) {
            emit(Resource.Loading(false))
            return@flow
        }

        val remoteListing = try {
            companyListingsParser.parse(stockService.getListings().byteStream())
        } catch (e: IOException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data"))
            null
        } catch (e: HttpException) {
            e.printStackTrace()
            emit(Resource.Error("Couldn't load data"))
            null
        }

        remoteListing?.let {
            stockDatabase.stockDao.apply {
                clearCompanyListings()
                insertCompanyListings(
                    it.map { it.toCompanyListingEntity() }
                )
                emit(
                    Resource.Success(
                    data = searchCompanyListing("").map { it.toCompanyListing() }
                ))
                emit(Resource.Loading(false))
            }
        }
    }

    override suspend fun getIntraDayInfo(symbol: String): Resource<List<IntraDayInfo>> {
        return try {
            Resource.Success(intraDayInfoParser.parse(stockService.getIntraDayInfo(symbol).byteStream()))
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load intra day info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Couldn't load intra day info")
        }
    }

    override suspend fun getCompanyInfo(symbol: String): Resource<CompanyInfo> {
        return try {
            Resource.Success(stockService.getCompanyInfo(symbol).toCompanyInfo())
        } catch (e: IOException) {
            e.printStackTrace()
            Resource.Error("Couldn't load company info")
        } catch (e: HttpException) {
            e.printStackTrace()
            Resource.Error("Couldn't load company info")
        }
    }
}