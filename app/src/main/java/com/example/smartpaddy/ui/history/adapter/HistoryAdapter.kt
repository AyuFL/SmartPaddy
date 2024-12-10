package com.example.smartpaddy.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smartpaddy.data.response.DataResponse

class HistoryAdapter : Adapter<HistoryViewHolder>() {

  private var historyList: List<DataResponse> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
    return HomeViewHolder.create(parent)
  }

  override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
    val historyItem = historyList[position].result
    holder.bind(historyItem)
  }

  override fun getItemCount(): Int {
    return historyList.size
  }

  fun setHistoryList(historyList: List<DataResponse>) {
    this.historyList = historyList
    notifyItemRangeInserted(0, historyList.size)
  }
}