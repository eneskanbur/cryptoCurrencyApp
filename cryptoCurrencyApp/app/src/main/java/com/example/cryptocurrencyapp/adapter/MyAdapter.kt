package com.example.cryptocurrencyapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cryptocurrencyapp.databinding.CurrencyViewBinding
import com.example.cryptocurrencyapp.model.Currency
import com.example.cryptocurrencyapp.view.CurrencyDetails
import com.example.cryptocurrencyapp.view.CurrencyListDirections

class MyAdapter (val currencyList: List<Currency>) : RecyclerView.Adapter<MyAdapter.myViewHolder>(){

    class myViewHolder (val binding: CurrencyViewBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val currencyViewBinding = CurrencyViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return myViewHolder(currencyViewBinding)
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.binding.currencyNameTextView.text = currencyList[position].symbol

        Glide.with(holder.itemView.context)
            .load(currencyList[position].image)
            .into(holder.binding.currencyImage)

        holder.itemView.setOnClickListener {
            val action = CurrencyListDirections.actionCurrencyListToCurrencyDetails(currencyList[position])
            Navigation.findNavController(it).navigate(action)
        }
    }
}