package com.example.cryptocurrencyapp.viewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.model.Currency
import com.example.cryptocurrencyapp.service.CurrencyAPIService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CurrencyListViewModel: ViewModel() {

    private val currrencyAPIService = CurrencyAPIService()
    val currencies = MutableLiveData<List<Currency>>()
    val currenciesLoading = MutableLiveData<Boolean>()


    fun getCurrencies(){
        viewModelScope.launch(Dispatchers.IO) {
            currenciesLoading.value = true
           val getCurrencies = currrencyAPIService.getData()
            withContext(Dispatchers.Main){
                currenciesLoading.value = false
                currencies.value = getCurrencies
            }
        }
    }

}