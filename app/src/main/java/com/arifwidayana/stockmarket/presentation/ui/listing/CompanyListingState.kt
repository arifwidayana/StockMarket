package com.arifwidayana.stockmarket.presentation.ui.listing

import com.arifwidayana.stockmarket.data.network.model.CompanyListing

data class CompanyListingState(
    val companies: List<CompanyListing> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val searchQuery: String = ""
)