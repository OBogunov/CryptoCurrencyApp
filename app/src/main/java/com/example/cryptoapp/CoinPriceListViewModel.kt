package com.example.cryptoapp

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cryptoapp.api.ApiService
import com.example.cryptoapp.database.CoinPriceInfoDao
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.example.cryptoapp.pojo.CoinPriceInfoRawData
import com.google.gson.Gson
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

class CoinPriceListViewModel(
    private val coinPriceInfoDao: CoinPriceInfoDao,
    private val apiService: ApiService
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    val priceList = coinPriceInfoDao.getPriceList()

    init {
        loadData()
    }

//    fun getDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
//        return coinPriceInfoDao.getPriceInfoAboutCoin(fSym)
//    }

    private fun loadData() {
        val disposable = apiService.getTopCoinsInfo(limit = 50)
            .map { coinInfoList -> coinInfoList.data?.map { it.coinInfo?.name }?.joinToString(",") }
            .flatMap { apiService.getFullPriceList(fSyms = it) }
            .map { getPriceListFromRawData(it) }
            .delaySubscription(10, TimeUnit.SECONDS)
            .repeat()
            .retry()
            .subscribeOn(Schedulers.io())
            .subscribe({
                coinPriceInfoDao.insertPriceList(it)
                Log.d("TEST_OF_LOADING_DATA", "Success: $it")
            }, {
                Log.d("TEST_OF_LOADING_DATA", "Failure: ${it.message}")
            })

        compositeDisposable.add(disposable)
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject ?: return result
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}