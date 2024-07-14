package com.aamir.ashraf.stockmarketapp.features_stocks.data.csv

import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanyListingParser @Inject constructor():CSVParser<CompanyListing> {
    override suspend fun parse(stream: InputStream): List<CompanyListing> {
        return withContext(Dispatchers.IO){
            val csvReader = CSVReader(InputStreamReader(stream))
             csvReader.readAll()
                .drop(1)  //drop first row because it contain filed like name etc
                .mapNotNull {row->
                    val symbol = row.getOrNull(0)
                    val name = row.getOrNull(1)
                    val exchange = row.getOrNull(2)
                    CompanyListing(
                        symbol = symbol?:return@mapNotNull null,
                        name = name?:return@mapNotNull null,
                        exchange = exchange?:return@mapNotNull null
                    )
                }.also {
                    csvReader.close()
                }
        }
    }
}