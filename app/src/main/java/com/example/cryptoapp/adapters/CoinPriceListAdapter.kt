package com.example.cryptoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.cryptoapp.R
import com.example.cryptoapp.pojo.CoinPriceInfo
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_coin_prce_list.view.*
import kotlinx.android.synthetic.main.item_coin_info.view.*

class CoinPriceListAdapter(
    private val onCoinClickListener: (CoinPriceInfo) -> Unit
) : RecyclerView.Adapter<CoinPriceListAdapter.CoinInfoViewHolder>() {

    var coinInfoList: List<CoinPriceInfo> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoinInfoViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_coin_info, parent, false)
        return CoinInfoViewHolder(view)
    }

    override fun getItemCount() = coinInfoList.size

    override fun onBindViewHolder(holder: CoinInfoViewHolder, position: Int) {
        val coin = coinInfoList[position]
        holder.bind(coin)
    }

    inner class CoinInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val ivLogoCoin = itemView.ivLogoCoin
        private val tvSymbols = itemView.tvSymbols
        private val tvPrice = itemView.tvPrice
        private val tvLastUpdate = itemView.tvLastUpdate

        fun bind(coinPriceInfo: CoinPriceInfo) {
            with(coinPriceInfo) {
                val symbolsTemplate = itemView.context.getString(R.string.symbols_template)
                val lastUpdateTemplate =
                    itemView.context.resources.getString(R.string.last_update_template)
                tvSymbols.text = String.format(symbolsTemplate, fromSymbol, toSymbol)
                tvPrice.text = price
                tvLastUpdate.text = String.format(lastUpdateTemplate, getFormattedTime())
                Picasso.get().load(getFullImageUrl()).into(ivLogoCoin)
                itemView.setOnClickListener {
                    onCoinClickListener(coinPriceInfo)
                }
            }
        }
    }
}