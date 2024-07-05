package com.example.cryptocurrencyapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.model.Currency
import com.example.cryptocurrencyapp.service.CurrencyAPIService
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
                    showToastMessage(contex,errorMessage.value.toString())
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value = "Sorgu Sınırına Ulaşıldı: ${e.message}"
                    showToastMessage(contex,errorMessage.value.toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value = "Beklenmeyen hata: ${e.message}"
                    showToastMessage(contex,errorMessage.value.toString())
                }
            }
        }
    }

    fun showToastMessage(contex : Context, errorMessage: String, ){
        Toast.makeText(contex,errorMessage,Toast.LENGTH_LONG).show()
    }

}
