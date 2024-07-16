package com.example.cryptocurrencyapp.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class StockData(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("values")
    val values: List<Value>
) : Parcelable

@Parcelize
data class Meta(
    @SerializedName("symbol")
    val symbol: String,
    @SerializedName("interval")
    val interval: String,
    @SerializedName("currency")
    val currency: String,
    @SerializedName("exchange_timezone")
    val exchangeTimezone: String,
    @SerializedName("exchange")
    val exchange: String,
    @SerializedName("mic_code")
    val micCode: String,
    @SerializedName("type")
    val type: String
) : Parcelable

@Parcelize
data class Value(
    @SerializedName("datetime")
    val datetime: String,
    @SerializedName("open")
    val open: Float,
    @SerializedName("high")
    val high: Float,
    @SerializedName("low")
    val low: Float,
    @SerializedName("close")
    val close: Float,
    @SerializedName("volume")
    val volume: Float
) : Parcelable
