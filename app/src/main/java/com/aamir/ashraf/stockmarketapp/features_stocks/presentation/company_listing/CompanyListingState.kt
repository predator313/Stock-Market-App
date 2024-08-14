package com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_listing

import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing

data class CompanyListingState(
    val companies:List<CompanyListing> = emptyList(),
    val isLoading:Boolean = false,
    val isRefreshing:Boolean = false,
    val searchQuery:String = ""
)
