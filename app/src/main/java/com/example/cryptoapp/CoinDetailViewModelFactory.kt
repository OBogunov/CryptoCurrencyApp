package com.example.cryptoapp

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cryptoapp.database.AppDatabase

class CoinDetailViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val coinPriceInfoDao = AppDatabase.getInstance(context).coinPriceInfoDao()
        val viewModel = CoinDetailViewModel(coinPriceInfoDao)
        return viewModel as T
    }
}