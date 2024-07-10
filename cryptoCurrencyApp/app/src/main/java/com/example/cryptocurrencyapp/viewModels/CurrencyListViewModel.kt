package com.example.cryptocurrencyapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.model.Currency
import com.example.cryptocurrencyapp.service.CurrencyAPIService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class CurrencyListViewModel : ViewModel() {

    private val currencyAPIService = CurrencyAPIService()
    val currencies = MutableLiveData<List<Currency>>()
    val currenciesLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    private val db = Firebase.firestore


    fun getCurrencies(contex: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                currenciesLoading.postValue(true)
                val getCurrencies = currencyAPIService.getData()
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    currencies.value = getCurrencies
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value = "İnternet bağlantısı hatası: ${e.message}"
                    showToastMessage(contex, errorMessage.value.toString())
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value = "Sorgu Sınırına Ulaşıldı: ${e.message}"
                    showToastMessage(contex, errorMessage.value.toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value = "Beklenmeyen hata: ${e.message}"
                    showToastMessage(contex, errorMessage.value.toString())
                }
            }
        }
    }

    fun showToastMessage(context: Context, errorMessage: String, ) {
        viewModelScope.launch(Dispatchers.Main) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }

    fun updateCurrencies(context: Context) {
        viewModelScope.launch(Dispatchers.Main) {
            currencies.value?.forEach { currency ->
                if (currency.priceStorage != null) {
                    if (currency.priceStorage!!.size == 5) {
                        for (i in 1..4) {
                            currency.priceStorage!!.add(i - 1, currency.priceStorage!!.get(i))
                        }
                        currency.priceStorage!!.add(4, currency.currentPrice.toFloat())
                    }else {
                        currency.priceStorage!!.add(currency.currentPrice.toFloat())
                    }

//                        if (currency.priceStorage[4] != 0.0F) {
//                            for (i in 1..4) {
//                                currency.priceStorage[i - 1] = currency.priceStorage[i]
//                            }
//                            currency.priceStorage[currency.priceStorage.size - 1] = currency.currentPrice.toFloat()
//                        } else {
//                            currency.priceStorage[4] = currency.currentPrice.toFloat()
//                        }
                    } else {
                        showToastMessage(context, "priceStorage arrayinin boyutu yetersiz.")
                    }
                }
            }
        }


    fun saveCurrencyListHistory(context: Context) {

        viewModelScope.launch(Dispatchers.IO) {
            for (currency in currencies.value!!) {
                //var a = mutableListOf(0.0F,0.0F,0.0F,0.0F,0.0F)
                val priceHistory = hashMapOf(
                    "symbol" to currency.symbol,
                    "priceStorage" to getPriceStorageWithSymbol(currency.symbol,context) //?: mutableListOf(currency.currentPrice.toFloat(),0.0F,0.0F,currency.currentPrice.toFloat(),0.0F) )
                )
                try {
                    db.collection("priceListHistory")
                        .document(currency.symbol)
                        .set(priceHistory)
                        .addOnSuccessListener {
                            println(currency.symbol)
                            showToastMessage(context, "Veri başarıyla kaydedildi.")
                        }
                        .addOnFailureListener { e ->
                            showToastMessage(context, "Veri kaydı başarısız oldu: ${e.message}")
                        }
                } catch (e: Exception) {
                    showToastMessage(context, "Beklenmeyen hata: ${e.message}")
                }
            }
        }
    }

    fun downLoadCurrencyListHistory(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("priceListHistory")
                    .get()
                    .addOnSuccessListener { documents ->
                        for (document in documents) {
                            val symbol = document.id
                            val priceStorage = document.data["priceStorage"]
                            //val priceStorageArray = priceStorage.mapNotNull { it as? Float }.toMutableList()
                            //currencies.value?.find { it.symbol == symbol }?.priceStorage = priceStorage
                        }
                    }
                    .addOnFailureListener { e ->
                        showToastMessage(context, "Veri alınırken hata oldu: ${e.message}")
                    }
            } catch (e: Exception) {
                showToastMessage(context, "Beklenmeyen hata: ${e.message}")
            }
        }

    }

    private fun getPriceStorageWithSymbol(symbol: String,context: Context) : ArrayList<Float>{
        var result : ArrayList<Float> = arrayListOf()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                db.collection("priceListHistory").document(symbol)
                    .get()
                    .addOnSuccessListener { document ->
                        result = document.data?.mapNotNull {  it as? Float } as ArrayList<Float> }
                    .addOnFailureListener {
                         showToastMessage(context,"Veri çevirmede hata oldu.")
                    }
            }catch (e: Exception){
                Toast.makeText(context,"Sembol ile çekilirken hata oluştu.",Toast.LENGTH_LONG).show()
            }
        }
        return result
    }
}
