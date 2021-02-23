package com.example.cryptoapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_detail.*

class CoinDetailActivity : AppCompatActivity() {

    private val viewModelFactory by lazy { CoinDetailViewModelFactory(this) }

    private val viewModel by viewModels<CoinDetailViewModel> { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!intent.hasExtra(EXTRA_FROM_SYMBOL)) {
            finish()
            return
        }
        setContentView(R.layout.activity_coin_detail)
        val fromSymbol = intent.getStringExtra(EXTRA_FROM_SYMBOL)!!
        viewModel.getDetailInfo(fromSymbol).observe(this, {
            setCoinDetails(it)
        })
    }

    private fun setCoinDetails(coinPriceInfo: CoinPriceInfo) {
        coinPriceInfo.also {
            tvPrice.text = it.price
            tvMinPrice.text = it.lowDay
            tvMaxPrice.text = it.highDay
            tvLastMarket.text = it.lastMarket
            tvLastUpdate.text = it.getFormattedTime()
            tvFromSymbol.text = it.fromSymbol
            tvToSymbol.text = it.toSymbol
            Picasso.get().load(it.getFullImageUrl()).into(ivLogoCoin)
        }
    }

    companion object {

        private const val EXTRA_FROM_SYMBOL = "fSym"

        fun newIntent(context: Context, fromSymbol: String): Intent {
            val intent = Intent(context, CoinDetailActivity::class.java)
            intent.putExtra(EXTRA_FROM_SYMBOL, fromSymbol)
            return intent
        }
    }
}
