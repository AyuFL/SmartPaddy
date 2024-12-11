package com.example.smartpaddy.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.smartpaddy.data.response.ResultResponse
import com.example.smartpaddy.databinding.ItemHistoryBinding

class HomeViewHolder(private var binding: ItemHistoryBinding) : ViewHolder(binding.root) {

  fun bind(historyItem: ResultResponse, imageUrl: String) {
    binding.let {
      it.accuracyTv.text = "${(historyItem.predictedProb * 100).toString().take(2)}%"
      it.classTv.text = historyItem.predictedClass
      it.descTv.text = historyItem.penjelasan

      Glide.with(it.root)
        .load(imageUrl)
        .into(it.paddyIv)
    }
  }

  companion object {
    fun create(view: ViewGroup): HomeViewHolder {
      val inflater = LayoutInflater.from(view.context)
      val binding = ItemHistoryBinding.inflate(inflater, view, false)
      return HomeViewHolder(binding)
    }
  }
}