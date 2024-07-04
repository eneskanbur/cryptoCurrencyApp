package com.example.cryptocurrencyapp.service

import com.example.cryptocurrencyapp.model.Currency
import retrofit2.http.GET

interface CurrencyAPI {
    //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json
    @GET("https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json")
    suspend fun getCurrency(): List<Currency>

}