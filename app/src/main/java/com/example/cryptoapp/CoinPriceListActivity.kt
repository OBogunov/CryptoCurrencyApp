package com.example.cryptoapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.adapters.CoinPriceListAdapter
import kotlinx.android.synthetic.main.activity_coin_prce_list.*

class CoinPriceListActivity : AppCompatActivity() {

    private val adapter = CoinPriceListAdapter { coinPriceInfo ->
        val intent = CoinDetailActivity.newIntent(
            this@CoinPriceListActivity,
            coinPriceInfo.fromSymbol
        )
        startActivity(intent)
    }

    private val viewModelFactory by lazy { CoinPriceListViewModelFactory(this) }

    private val viewModel by viewModels<CoinPriceListViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coin_prce_list)
        rvCoinPriceList.adapter = adapter
        viewModel.priceList.observe(this, {
            if (it.isNotEmpty()) {
                progressBarSpinner.hide()
                adapter.coinInfoList = it
            }
        })
    }

}
