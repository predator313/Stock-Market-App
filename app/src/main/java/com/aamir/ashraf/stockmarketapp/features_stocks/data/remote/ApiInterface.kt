package com.aamir.ashraf.stockmarketapp.features_stocks.data.remote

import com.aamir.ashraf.stockmarketapp.core.API_KEY
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apikey")apikey:String = API_KEY
    ):ResponseBody   //response body is used to get access to the file stream
}