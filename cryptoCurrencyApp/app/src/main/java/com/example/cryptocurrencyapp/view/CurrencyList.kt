package com.example.cryptocurrencyapp.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cryptocurrencyapp.adapter.MyAdapter
import com.example.cryptocurrencyapp.databinding.FragmentCurrencyListBinding
import com.example.cryptocurrencyapp.model.Currency
import com.example.cryptocurrencyapp.viewModels.CurrencyListViewModel

class CurrencyList : Fragment() {

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!
    private lateinit var currList : List<Currency>
    private lateinit var viewModel : CurrencyListViewModel
    private lateinit var adapter : MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CurrencyListViewModel::class.java]
        viewModel.getCurrencies()


       /* CoroutineScope(Dispatchers.IO).launch {
            currList = retrofit.getCurrency()
            currList.forEach {
                println(it.currency)
            }
        }*/

        adapter = MyAdapter(currList)
        binding.recyclerView.layoutManager = LinearLayoutManager(this@CurrencyList.context)
        binding.recyclerView.adapter = adapter
        binding.refreshLayout.setOnRefreshListener {
            binding.recyclerView.visibility = View.GONE
            binding.ListProgressBar.visibility = View.VISIBLE
            viewModel.getCurrencies()
            binding.refreshLayout.isRefreshing = false
        }
    }

    private fun observerLiveData(){
            viewModel.currencies.observe(viewLifecycleOwner){
            adapter = MyAdapter(it)
            binding.recyclerView.visibility = View.VISIBLE
        }

        viewModel.currenciesLoading.observe(viewLifecycleOwner){
            if (it){
                binding.recyclerView.visibility = View.GONE
                binding.ListProgressBar.visibility = View.VISIBLE
            }else{
                binding.ListProgressBar.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}