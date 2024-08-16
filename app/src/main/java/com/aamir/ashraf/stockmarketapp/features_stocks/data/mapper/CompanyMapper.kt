package com.aamir.ashraf.stockmarketapp.features_stocks.data.mapper

import com.aamir.ashraf.stockmarketapp.features_stocks.data.local.CompanyListingEntity
import com.aamir.ashraf.stockmarketapp.features_stocks.data.remote.dto.CompanyInfoDto
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyInfo
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing

fun CompanyListingEntity.toCompanyListing():CompanyListing{
    return CompanyListing(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}

fun CompanyListing.toCompanyListingEntity():CompanyListingEntity{
    return CompanyListingEntity(
        name = name,
        symbol = symbol,
        exchange = exchange
    )
}
fun CompanyInfoDto.toCompanyInfo(): CompanyInfo {
    return CompanyInfo(
        symbol = symbol ?: "",
        description = description ?: "",
        name = name ?: "",
        country = country ?: "",
        industry = industry ?: ""
    )
}