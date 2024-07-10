package com.example.cryptocurrencyapp.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptocurrencyapp.databinding.FragmentGraphPageBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.EntryXComparator
import java.util.Collections

class GraphPage : Fragment() {
    private var _binding: FragmentGraphPageBinding? = null
    private val binding get() = _binding!!
    private lateinit var lineChart : LineChart
    private lateinit var priceList: FloatArray
    var graphList : MutableList<Entry> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGraphPageBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lineChart = binding.lineChart

        arguments?.let {//priceListteki verileri grafiğe ekleme
            priceList = GraphPageArgs.fromBundle(it).priceList

            for (index in priceList.withIndex()){
                graphList.add(Entry(index.value,index.index.toFloat()))
            }

            // TODO: çözüm
            //Collections.sort(graphList, EntryXComparator()) Bu sorunu çözüyor ama nasıl?

            val dataSet = LineDataSet(graphList, "Price History")
            dataSet.color = Color.RED
            dataSet.lineWidth = 2.5f
            dataSet.fillAlpha = 110
            dataSet.fillColor = Color.RED

            val lineData = LineData(dataSet)
            lineChart.data = lineData
            lineChart.legend.isEnabled = true
            lineChart.legend.textSize = 14f
            lineChart.legend.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
            lineChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
            lineChart.invalidate() // Refresh chart

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}