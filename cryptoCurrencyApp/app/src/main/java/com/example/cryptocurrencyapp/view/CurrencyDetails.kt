package com.example.cryptocurrencyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.databinding.FragmentCurrencyDetailsBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

class CurrencyDetails : Fragment() {
    private var _binding: FragmentCurrencyDetailsBinding? = null
    private val binding get() = _binding!!
    var priceList : FloatArray = FloatArray(5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        arguments?.let {//pricelist diye kendi classında tutabilir?
            val currency = CurrencyDetailsArgs.fromBundle(it)
            var price = currency.currency.currentPrice.toFloat()

            if (index < priceList.size) {
                priceList[index] = price
            } else {
                for (i in 1 until priceList.size) {
                    priceList[i - 1] = priceList[i]
                }
                priceList[priceList.size - 1] = price
            }
            indexUp()

            binding.textId.text = "ID: " + currency.currency.id
            binding.textSymbol.text = "Symbol: " + currency.currency.symbol
            binding.textName.text = "Name: " + currency.currency.name

            Glide.with(requireContext())
                .load(currency.currency.image)
                .into(binding.textImage)

            binding.textCurrentPrice.text = "Current Price: " + currency.currency.currentPrice.toString()
            binding.textMarketCap.text = "Market Cap: " + currency.currency.marketCap.toString()
            binding.textMarketCapRank.text = "Market Cap Rank: " + currency.currency.marketCapRank.toString()
            binding.textFullyDilutedValuation.text = "Fully Diluted Valuation: " + currency.currency.fullyDilutedValuation.toString()
            binding.textTotalVolume.text = "Total Volume: " + currency.currency.totalVolume.toString()
            binding.textHigh24h.text = "High 24h: " + currency.currency.high24h.toString()
            binding.textLow24h.text = "Low 24h: " + currency.currency.low24h.toString()
            binding.textPriceChange24h.text = "Price Change 24h: " + currency.currency.priceChange24h.toString()
            binding.textPriceChangePercentage24h.text = "Price Change Percentage 24h: " + currency.currency.priceChangePercentage24h.toString()
            binding.textMarketCapChange24h.text = "Market Cap Change 24h: " + currency.currency.marketCapChange24h.toString()
            binding.textMarketCapChangePercentage24h.text = "Market Cap Change Percentage 24h: " + currency.currency.marketCapChangePercentage24h.toString()
            binding.textCirculatingSupply.text = "Circulating Supply: " + currency.currency.circulatingSupply.toString()
            binding.textTotalSupply.text = "Total Supply: " + currency.currency.totalSupply.toString()
            binding.textMaxSupply.text = "Max Supply: " + currency.currency.maxSupply.toString()
            binding.textAth.text = "All-Time High (ATH): " + currency.currency.ath.toString()
            binding.textAthChangePercentage.text = "ATH Change Percentage: " + currency.currency.athChangePercentage.toString()
            binding.textAthDate.text = "ATH Date: " + currency.currency.athDate
            binding.textAtl.text = "All-Time Low (ATL): " + currency.currency.atl.toString()
            binding.textAtlChangePercentage.text = "ATL Change Percentage: " + currency.currency.atlChangePercentage.toString()
            binding.textAtlDate.text = "ATL Date: " + currency.currency.atlDate
            binding.textLastUpdated.text = "Last Updated: " + currency.currency.lastUpdated

        }
        binding.showGraphButton.setOnClickListener {
            showGraph(it)
        }
    }

    fun showGraph(view: View){
        val action = CurrencyDetailsDirections.actionCurrencyDetailsToGraphPage(priceList)
        Navigation.findNavController(view).navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        var index: Int = 0

        fun indexUp() {
            index++
        }
    }
}