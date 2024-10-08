package com.aamir.ashraf.stockmarketapp.features_stocks.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.aamir.ashraf.stockmarketapp.features_stocks.data.remote.dto.IntraDayInfoDto
import com.aamir.ashraf.stockmarketapp.features_stocks.domain.model.IntraDayInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
fun IntraDayInfoDto.toIntraDayInfo():IntraDayInfo{
    val pattern = "yyyy-MM-dd HH:mm:ss"
    val formatter = DateTimeFormatter.ofPattern(pattern, Locale.getDefault())
    val localDateTime = LocalDateTime.parse(timeStamp,formatter)
    return IntraDayInfo(
        date = localDateTime,
        close = close
    )
}