package com.example.cryptocurrencyapp.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.databinding.FragmentCurrencyDetailsBinding
import com.example.cryptocurrencyapp.model.StockData
import com.example.cryptocurrencyapp.viewModels.CurrencyListViewModel
import com.example.cryptocurrencyapp.viewModels.GraphPageViewModel
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.animation.Easing.EaseOutBack
import com.github.mikephil.charting.animation.Easing.EasingFunction
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.utils.EntryXComparator
import java.util.Collections

class CurrencyDetails : Fragment() {
    private var _binding: FragmentCurrencyDetailsBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel : CurrencyListViewModel
    private lateinit var lineChart : LineChart
    var graphList : MutableList<Entry> = mutableListOf()
    private lateinit var viewModelGraphPage : GraphPageViewModel
    private var xValues = arrayListOf<String>()


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
        viewModelGraphPage = ViewModelProvider(this)[GraphPageViewModel::class.java]
        viewModel = ViewModelProvider(this)[CurrencyListViewModel::class.java]

        arguments?.let {
            val currency = CurrencyDetailsArgs.fromBundle(it)
            viewModelGraphPage.getHistoricalData(requireContext(),currency.currency.symbol)
            obsereLiveData()

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun obsereLiveData() {
        viewModelGraphPage.historicalData.observe(viewLifecycleOwner) {
            updateChart(it)
        }
    }

    private fun updateChart(data: StockData) {
        lineChart = binding.lineChart
        graphList.clear()
        xValues.clear()

        data.values.reversed().forEachIndexed { index, value ->
            graphList.add(Entry( index.toFloat(), value.high))
            xValues.add(value.datetime.substring(5))
        }


        Collections.sort(graphList,EntryXComparator())
        val dataSet = LineDataSet(graphList, "Price")
        dataSet.color = Color.RED
        dataSet.valueTextColor = Color.WHITE
        dataSet.lineWidth = 2.5f
        dataSet.fillAlpha = 110
        dataSet.fillColor = Color.RED

        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val description: Description = lineChart.description
        description.text = "${data.meta.symbol} price history"
        lineChart.description = description
        description.textSize = 14f
        description.textColor = Color.WHITE
        description.setPosition(450f, 30f)

        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(xValues)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.setLabelCount(xValues.size, true)
        xAxis.granularity = 15f
        xAxis.textColor = Color.WHITE

        lineChart.legend.isEnabled = true
        lineChart.legend.textSize = 14f
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.axisRight.setDrawLabels(false)
        lineChart.legend.textColor = Color.WHITE
        lineChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        lineChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        lineChart.animateXY(2000,2000)
        lineChart.invalidate()
    }




}