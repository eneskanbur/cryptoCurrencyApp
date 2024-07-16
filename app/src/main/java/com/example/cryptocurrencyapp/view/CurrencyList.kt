package com.example.cryptocurrencyapp.view

import android.os.Bundle
import android.os.Parcelable
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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class CurrencyList : Fragment() {

    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!
    private var currList : List<Currency> = emptyList()
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
        viewModel.getCurrencies(requireContext())

        with(binding) {
            adapter = MyAdapter(currList)
            recyclerView.layoutManager = LinearLayoutManager(this@CurrencyList.context)
            recyclerView.adapter = adapter
            appName.visibility = View.GONE
            logOut.visibility = View.GONE

            refreshLayout.setOnRefreshListener {
                recyclerView.visibility = View.GONE
                appName.visibility = View.GONE
                appName.visibility = View.GONE
                ListProgressBar.visibility = View.VISIBLE
                viewModel.getCurrencies(requireContext())
                refreshLayout.isRefreshing = false
            }
        }

        binding.logOut.setOnClickListener {
            logOut(it)
        }

        observerLiveData()
    }

    private fun observerLiveData(){
        viewModel.currencies.observe(viewLifecycleOwner){
            adapter = MyAdapter(it)
            with(binding){
                recyclerView.layoutManager = LinearLayoutManager(this@CurrencyList.context)
                recyclerView.adapter = adapter
                logOut.visibility = View.VISIBLE
                appName.visibility = View.VISIBLE
                recyclerView.visibility = View.VISIBLE
            }
        }

        viewModel.currenciesLoading.observe(viewLifecycleOwner){
            with(binding){
                if (it){
                    recyclerView.visibility = View.GONE
                    logOut.visibility = View.GONE
                    appName.visibility = View.GONE
                    ListProgressBar.visibility = View.VISIBLE
                }else{
                    ListProgressBar.visibility = View.GONE
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun logOut(view: View){
        Firebase.auth.signOut()
        activity?.finish()
    }

}