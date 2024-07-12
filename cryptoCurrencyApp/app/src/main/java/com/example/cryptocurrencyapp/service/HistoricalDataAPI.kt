package com.example.cryptocurrencyapp.service

import com.example.cryptocurrencyapp.model.StockData
import retrofit2.http.GET
import retrofit2.http.Query

interface HistoricalDataAPI {
    @GET("time_series")
    suspend fun getHistoricalPrice(
        @Query("symbol") symbol: String,
        @Query("interval") interval: String,
        @Query("start_date") startDate: String,
        @Query("outputsize") outputSize: String,
        @Query("apikey") apiKey: String
    ): StockData
}
