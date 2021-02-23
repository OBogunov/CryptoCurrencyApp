package com.example.cryptoapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.database.CoinPriceInfoDao
import com.example.cryptoapp.pojo.CoinPriceInfo

class CoinDetailViewModel(
    private val coinPriceInfoDao: CoinPriceInfoDao
) : ViewModel() {

    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return coinPriceInfoDao.getPriceInfoAboutCoin(fSym)
    }
}