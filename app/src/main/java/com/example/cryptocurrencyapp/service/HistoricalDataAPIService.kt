package com.example.cryptocurrencyapp.service

import android.content.Context
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.model.StockData
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class HistoricalDataAPIService {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.twelvedata.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val api = retrofit.create(HistoricalDataAPI::class.java)

    suspend fun getHistoricalPrice(symbol : String, context: Context): StockData {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DAY_OF_YEAR, -10)
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formattedDate = dateFormat.format(calendar.time)
        return api.getHistoricalPrice(
            symbol = "${symbol}/usd",
            interval = "1day",
            startDate = formattedDate,
            outputSize = "10",
            apiKey = context.getString(R.string.b10affdca65045bcbcedbc8fa142556c)
        )
    }
}
