package com.example.cryptocurrencyapp.service

import com.example.cryptocurrencyapp.model.Currency
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CurrencyAPIService {

    private val retrofit = Retrofit.Builder().baseUrl("https://api.coingecko.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(CurrencyAPI::class.java)

    suspend fun getData() : List<Currency>{
        return retrofit.getCurrency()
    }


}

