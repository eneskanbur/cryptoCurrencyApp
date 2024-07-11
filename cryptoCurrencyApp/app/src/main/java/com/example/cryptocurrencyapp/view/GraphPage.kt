package com.example.cryptocurrencyapp.view

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
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
    private var id : String = ""


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
        val webView: WebView = binding.coinGeckoWebView
        webView.settings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()
        arguments?.let {
            id = GraphPageArgs.fromBundle(it).id
        }

        val htmlData = """
        <html>
        <head>
            <script src="https://widgets.coingecko.com/gecko-coin-price-chart-widget.js"></script>
        </head>
        <body>
            <gecko-coin-price-chart-widget locale="en" outlined="true" coin-id="${id}" initial-currency="usd" width="0" height="0"></gecko-coin-price-chart-widget>
        </body>
        </html>
    """

        webView.loadDataWithBaseURL("https://widgets.coingecko.com", htmlData, "text/html", "UTF-8", null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}