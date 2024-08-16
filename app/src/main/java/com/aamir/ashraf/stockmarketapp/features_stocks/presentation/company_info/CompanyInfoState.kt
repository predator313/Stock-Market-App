package com.aamir.ashraf.stockmarketapp.features_stocks.presentation.company_info

import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyInfo
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.IntraDayInfo

data class CompanyInfoState(
    val stockInfos:List<IntraDayInfo> = emptyList(),
    val companyInfo: CompanyInfo?=null,
    val isLoading:Boolean = false,
    val error:String?=null
)