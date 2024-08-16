package com.aamir.ashraf.stockmarketapp.features_stocks.domain.repository

import com.aamir.ashraf.stockmarketapp.core.utils.Resource
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyInfo
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.IntraDayInfo
import kotlinx.coroutines.flow.Flow

interface StockRepository {
    suspend fun getCompanyListing(
        fetchFromRemote:Boolean,
        query:String
    ):Flow<Resource<List<CompanyListing>>>

    suspend fun getIntraDayInfo(
        symbol: String
    ): Resource<List<IntraDayInfo>>

    suspend fun getCompanyInfo(
        symbol: String
    ): Resource<CompanyInfo>
}