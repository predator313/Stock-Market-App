package com.aamir.ashraf.stockmarketapp.features_stocks.domain.model

import java.time.LocalDateTime

data class IntraDayInfo(
    val date:LocalDateTime,
    val close:Double
)
