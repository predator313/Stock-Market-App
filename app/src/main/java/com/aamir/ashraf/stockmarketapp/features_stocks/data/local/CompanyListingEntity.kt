package com.aamir.ashraf.stockmarketapp.features_stocks.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CompanyListingEntity(
    val name:String,
    val symbol:String,
    val exchange:String,
    @PrimaryKey val id:Int?=null
)