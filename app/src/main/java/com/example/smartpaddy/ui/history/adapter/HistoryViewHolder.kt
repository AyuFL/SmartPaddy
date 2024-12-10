package com.example.smartpaddy.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.smartpaddy.data.response.ResultResponse
import com.example.smartpaddy.databinding.ItemHistoryBinding

class HistoryViewHolder(private var binding: ItemHistoryBinding) : ViewHolder(binding.root) {
  fun bind(historyItem: ResultResponse) {
    binding.let {
      it.accuracyTv.text = "${(historyItem.predictedProb * 100).toString().take(2)}%"
      it.classTv.text = historyItem.predictedClass
    }
  }

  companion object {
    fun create(view: ViewGroup): HistoryViewHolder {
      val inflater = LayoutInflater.from(view.context)
      val binding = ItemHistoryBinding.inflate(inflater, view, false)
      return HistoryViewHolder(binding)
    }
  }
}