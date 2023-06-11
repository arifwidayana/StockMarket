package com.arifwidayana.stockmarket.presentation.ui.info

import com.arifwidayana.stockmarket.data.network.model.CompanyInfo
import com.arifwidayana.stockmarket.data.network.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfo: List<IntraDayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)