package com.example.cryptocurrencyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.cryptocurrencyapp.R
import com.example.cryptocurrencyapp.databinding.FragmentCurrencyDetailsBinding
import com.example.cryptocurrencyapp.databinding.FragmentCurrencyListBinding

class CurrencyDetails : Fragment() {
    private var _binding: FragmentCurrencyDetailsBinding? = null
    private val binding get() = _binding!!


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
        arguments?.let {
            val currency = CurrencyDetailsArgs.fromBundle(it)
            binding.textViewName.text = currency.currency
            binding.textViewPrice.text = currency.price
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}