package com.example.cryptocurrencyapp.service

import com.example.cryptocurrencyapp.model.Currency
import retrofit2.http.GET

interface CurrencyAPI {
    //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json
    @GET("https://api.coingecko.com/api/v3/coins/markets?vs_currency=usd&order=market_cap_desc&per_page=100&page=1&sparkline=false")
    suspend fun getCurrency(): List<Currency>

}