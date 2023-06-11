package com.arifwidayana.stockmarket.data.mapper

import com.arifwidayana.stockmarket.data.local.model.CompanyListingEntity
import com.arifwidayana.stockmarket.data.network.model.CompanyInfo
import com.arifwidayana.stockmarket.data.network.model.CompanyListing
import com.arifwidayana.stockmarket.data.network.model.dto.CompanyInfoDto

fun CompanyListingEntity.toCompanyListing(): CompanyListing =
    CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )

fun CompanyListing.toCompanyListingEntity(): CompanyListingEntity =
    CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )

fun CompanyInfoDto.toCompanyInfo(): CompanyInfo =
    CompanyInfo(
        name = name.orEmpty(),
        description = description.orEmpty(),
        symbol = symbol.orEmpty(),
        industry = industry.orEmpty(),
        country = country.orEmpty()
    )