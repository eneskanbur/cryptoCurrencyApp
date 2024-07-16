package com.example.cryptocurrencyapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.model.StockData
import com.example.cryptocurrencyapp.service.HistoricalDataAPI
import com.example.cryptocurrencyapp.service.HistoricalDataAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

class GraphPageViewModel : ViewModel(){

    private val historicalDataAPIService = HistoricalDataAPIService()
    val historicalData = MutableLiveData<StockData>()
    val historicalDataLoading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()

    fun getHistoricalData(context: Context,symbol : String){
        viewModelScope.launch(Dispatchers.IO) {
            try {
                historicalDataLoading.postValue(true)
                val getHistoricalData = historicalDataAPIService.getHistoricalPrice(symbol)
                withContext(Dispatchers.Main) {
                    historicalDataLoading.value = false
                    historicalData.value = getHistoricalData
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    historicalDataLoading.value = false
                    errorMessage.value = "İnternet bağlantısı hatası: ${e.message}"
                    showToastMessage(context, errorMessage.value.toString())
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    historicalDataLoading.value = false
                    errorMessage.value = "Sorgu Sınırına Ulaşıldı: ${e.message}"
                    showToastMessage(context, errorMessage.value.toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    historicalDataLoading.value = false
                    errorMessage.value = "Beklenmeyen hata: ${e.message}"
                    showToastMessage(context, errorMessage.value.toString())
                }
            }
        }
    }

    fun showToastMessage(context: Context,message : String){
        Toast.makeText(context,message,Toast.LENGTH_LONG).show()
    }


}