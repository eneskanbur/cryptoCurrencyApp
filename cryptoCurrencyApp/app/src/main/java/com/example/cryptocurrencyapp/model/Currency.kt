package com.example.cryptocurrencyapp.model

import com.google.gson.annotations.SerializedName

data class Currency (
    @SerializedName ("currency")
    val currency : String,
    @SerializedName ("price")
    val price : String
)


