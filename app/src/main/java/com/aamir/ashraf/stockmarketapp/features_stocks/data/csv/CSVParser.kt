package com.aamir.ashraf.stockmarketapp.features_stocks.data.csv

import java.io.InputStream
import java.io.OutputStream

interface CSVParser<T> {
    suspend fun  parse(stream: InputStream):List<T>
}