package com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository

import com.aamir.ashraf.stockmarketapp.core.utils.Resource
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListing(
        fetchFromRemote:Boolean,
        query:String
    ):Flow<Resource<List<CompanyListing>>>
}