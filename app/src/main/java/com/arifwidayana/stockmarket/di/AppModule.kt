package com.arifwidayana.stockmarket.di

import android.content.Context
import androidx.room.Room
import com.arifwidayana.stockmarket.data.local.StockDatabase
import com.arifwidayana.stockmarket.data.network.service.StockService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideStockService(): StockService {
        return Retrofit.Builder()
            .baseUrl(StockService.BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create())
            .client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    HttpLoggingInterceptor.Level.BASIC
                })
                .build())
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideStockDatabase(@ApplicationContext context: Context): StockDatabase {
        return Room.databaseBuilder(
            context = context,
            StockDatabase::class.java,
            "stock.db"
        ).build()
    }
}