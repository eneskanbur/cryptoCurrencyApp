package com.example.cryptocurrencyapp.viewModels

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cryptocurrencyapp.R
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
    private val errorMessage = MutableLiveData<String>()
    private val db = Firebase.firestore


    fun getCurrencies(contex: Context) {
        val context = contex
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
                    errorMessage.value =
                        context.getString(R.string.internet_baglantisi_hatasi, e.message)
                    showToastMessage(contex, errorMessage.value.toString())
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value =
                        context.getString(R.string.sorgu_sinirina_ulasildi, e.message)
                    showToastMessage(contex, errorMessage.value.toString())
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    currenciesLoading.value = false
                    errorMessage.value = context.getString(R.string.beklenmeyen_hata, e.message)
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
}
