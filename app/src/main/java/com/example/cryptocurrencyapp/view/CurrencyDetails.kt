package com.example.cryptocurrencyapp.view

import android.animation.Animator
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.databinding.FragmentCurrencyDetailsBinding
import com.example.cryptocurrencyapp.model.Currency
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
            val currency = CurrencyDetailsArgs.fromBundle(it).currency

            with(currency) {
                viewModelGraphPage.getHistoricalData(requireContext(),symbol)
                obsereLiveData()
                setupAnimation()
                setUI(CurrencyDetailsArgs.fromBundle(it).currency)
            }
        }

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

    }

    private fun setUI(currency: Currency) = binding.run {

        with(currency){
            textId.text = getString(R.string.id, id)
            textSymbol.text = getString(R.string.symbol, symbol)
            textName.text = getString(R.string.name, name)

            Glide.with(requireContext())
                .load(image)
                .into(textImage)

            textCurrentPrice.text = getString(R.string.current_price,currentPrice.toString())
            textMarketCap.text = getString(R.string.market_cap, marketCap.toString())
            textMarketCapRank.text = getString(R.string.market_cap_rank, marketCapRank.toString())
            textFullyDilutedValuation.text = getString(R.string.fully_diluted_valuation, fullyDilutedValuation.toString())
            textTotalVolume.text = getString(R.string.total_volume, totalVolume.toString())
            textHigh24h.text = getString(R.string.high_24h, high24h.toString())
            textLow24h.text = getString(R.string.low_24h, low24h.toString())
            textPriceChange24h.text = getString(R.string.price_change_24h, priceChange24h.toString())
            textPriceChangePercentage24h.text =
                getString(R.string.price_change_percentage_24h, priceChangePercentage24h.toString())
            textMarketCapChange24h.text = getString(R.string.market_cap_change_24h, marketCapChange24h.toString())
            textMarketCapChangePercentage24h.text =
                getString(R.string.market_cap_change_percentage_24h, marketCapChangePercentage24h.toString())
            textCirculatingSupply.text = getString(R.string.circulating_supply, circulatingSupply.toString())
            textTotalSupply.text = getString(R.string.total_supply, totalSupply.toString())
            textMaxSupply.text = getString(R.string.max_supply, maxSupply.toString())
            textAth.text = getString(R.string.all_time_high_ath, ath.toString())
            textAthChangePercentage.text = getString(R.string.ath_change_percentage, athChangePercentage.toString())
            textAthDate.text = getString(R.string.ath_date, athDate)
            textAtl.text = getString(R.string.all_time_low_atl, atl.toString())
            textAtlChangePercentage.text = getString(R.string.atl_change_percentage, atlChangePercentage.toString())
            textAtlDate.text = getString(R.string.atl_date, atlDate)
            textLastUpdated.text = getString(R.string.last_updated, lastUpdated)
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
        with(dataSet){
            color = Color.RED
            valueTextColor = Color.WHITE
            lineWidth = 2.5f
            fillAlpha = 110
            fillColor = Color.RED
        }


        val lineData = LineData(dataSet)
        lineChart.data = lineData

        val description: Description = lineChart.description
        with(description){
            text = getString(R.string.price_history, data.meta.symbol)
            textSize = 14f
            textColor = Color.WHITE
            setPosition(450f, 30f)
        }
        lineChart.description = description

        val xAxis = lineChart.xAxis
        with(xAxis){
            valueFormatter = IndexAxisValueFormatter(xValues)
            position = XAxis.XAxisPosition.BOTTOM
            setLabelCount(xValues.size, true)
            granularity = 15f
            textColor = Color.WHITE
        }

        with(lineChart){
            legend.isEnabled = true
            legend.textSize = 14f
            axisLeft.textColor = Color.WHITE
            axisRight.setDrawLabels(false)
            legend.textColor = Color.WHITE
            legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            animateXY(2000,2000)
            invalidate()
        }

    }

    private fun setupAnimation() {
        val animation = binding.graphAnimation
        animation.speed = 1.5f

        animation.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animator: Animator) {
                binding.lineChart.visibility = View.INVISIBLE
            }

            override fun onAnimationEnd(animator: Animator) {
                animation.visibility = View.GONE
                binding.lineChart.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animator: Animator) {
            }

            override fun onAnimationRepeat(animator: Animator) {
            }
        })
        animation.playAnimation()
    }


}