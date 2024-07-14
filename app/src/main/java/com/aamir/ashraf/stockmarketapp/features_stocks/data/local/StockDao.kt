package com.aamir.ashraf.stockmarketapp.features_stocks.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface StockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCompanyListings(
        companyListingEntities: List<CompanyListingEntity>
    )
    @Query("Delete  from companylistingentity")
    suspend fun clearCompanyListings()
    @Query("""
        select * from companylistingentity
        where lower(name) like '%' || lower(:query) || '%' or
        upper(:query) == symbol
    """)
    suspend fun searchCompanyListing(query:String):List<CompanyListingEntity>
}