package com.aamir.ashraf.stockmarketapp.features_stocks.data.csv

import android.os.Build
import androidx.annotation.RequiresApi
import com.aamir.ashraf.stockmarketapp.features_stocks.data.mapper.toIntraDayInfo
import com.aamir.ashraf.stockmarketapp.features_stocks.data.remote.dto.IntraDayInfoDto
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.CompanyListing
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.IntraDayInfo
import com.opencsv.CSVReader
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

class IntraDayInfoParser :CSVParser<IntraDayInfo> {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun parse(stream: InputStream): List<IntraDayInfo> {
        return withContext(Dispatchers.IO){
            val csvReader = CSVReader(InputStreamReader(stream))
             csvReader.readAll()
                .drop(1)  //drop first row because it contain filed like name etc
                .mapNotNull {row->
                    val timeStamp = row.getOrNull(0)?:return@mapNotNull null
                    val close = row.getOrNull(4)?:return@mapNotNull null
                    val dto = IntraDayInfoDto(
                        timeStamp = timeStamp,
                        close = close.toDouble()
                    )
                    dto.toIntraDayInfo()
                }
                 .filter {
                     it.date.dayOfMonth == LocalDateTime.now().minusDays(1).dayOfMonth
                 }
                 .sortedBy {
                     it.date.hour
                 }
                .also {
                    csvReader.close()
                }
        }
    }
}