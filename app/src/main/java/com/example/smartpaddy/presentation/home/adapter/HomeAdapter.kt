package com.example.smartpaddy.presentation.home.adapter

import android.content.Intent
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.smartpaddy.data.response.DataResponse
import com.example.smartpaddy.presentation.historyDetail.HistoryDetailActivity

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

    val postId = historyList[position].predictId

    holder.itemView.setOnClickListener {
      val intentToHistoryDetail = Intent(holder.itemView.context, HistoryDetailActivity::class.java)
      intentToHistoryDetail.putExtra("imageUrl", imageUrl)
      intentToHistoryDetail.putExtra("postId", postId)
      holder.itemView.context.startActivity(intentToHistoryDetail)
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