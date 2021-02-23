package com.example.cryptoapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.api.ApiFactory
import com.example.cryptoapp.database.AppDatabase

class CoinPriceListViewModelFactory(private val context: Context): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val coinPriceInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()
        val apiService = ApiFactory.apiService
        val viewModel = CoinPriceListViewModel(coinPriceInfoDao, apiService)
        return viewModel as T
    }
}