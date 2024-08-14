package com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_listing

sealed class CompanyListingEvent{
    object Refresh:CompanyListingEvent()
    data class OnSearchQueryChange(val query:String):CompanyListingEvent()
}
