package com.example.smartpaddy.presentation.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smartpaddy.data.response.DataResponse

class HomeAdapter : Adapter<HomeViewHolder>() {

  private var historyList: List<DataResponse> = listOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
    return HomeViewHolder.create(parent)
  }

  override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
    val imageUrl = historyList[position].imageUrl
    val historyItem = historyList[position].result

    if (imageUrl != null) {
      holder.bind(historyItem, imageUrl)
    }
  }

  override fun getItemCount(): Int {
    return historyList.size
  }

  fun setHistoryList(historyList: List<DataResponse>) {
    this.historyList = historyList
    notifyDataSetChanged()
  }
}