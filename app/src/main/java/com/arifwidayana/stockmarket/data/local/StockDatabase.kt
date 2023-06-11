package com.arifwidayana.stockmarket.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.arifwidayana.stockmarket.data.local.service.StockDao
import com.arifwidayana.stockmarket.data.local.model.CompanyListingEntity

@Database(
    entities = [CompanyListingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class StockDatabase : RoomDatabase() {
    abstract val stockDao: StockDao
}