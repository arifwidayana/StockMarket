package com.arifwidayana.stockmarket.presentation.ui.listing

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arifwidayana.stockmarket.common.wrapper.Resource
import com.arifwidayana.stockmarket.data.repository.StockRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompanyListingViewModel @Inject constructor(
    private val stockRepository: StockRepository
) : ViewModel() {
    var state by mutableStateOf(CompanyListingState())
    private var searchJob : Job? = null

    init {
        getCompanyListing()
    }

    fun onEvent(event: CompanyListingEvent) {
        when(event) {
            is CompanyListingEvent.Refresh -> {
                getCompanyListing(fetchFromNetwork = true)
            }
            is CompanyListingEvent.OnSearchQueryChange -> {
                state = state.copy(searchQuery = event.query)
                searchJob?.cancel()
                searchJob = viewModelScope.launch {
                    delay(500L)
                    getCompanyListing()
                }
            }
        }
    }

    private fun getCompanyListing(
        fetchFromNetwork: Boolean = false,
        query: String = state.searchQuery.lowercase()
    ) {
        viewModelScope.launch {
            stockRepository.getCompanyListings(fetchFromRemote = fetchFromNetwork, query)
                .collect {
                    when(it) {
                        is Resource.Success -> {
                            it.data?.let {
                                state = state.copy(
                                    companies = it
                                )
                            }
                        }
                        is Resource.Error -> Unit
                        is Resource.Loading -> {
                            state = state.copy(isLoading = it.isLoading)
                        }
                    }
                }
        }
    }
}